package com.example.delawaretrackandtraceapp.screens.detailsoforders

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT

import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
import com.example.delawaretrackandtraceapp.database.orders.asOrder
import com.example.delawaretrackandtraceapp.databinding.FragmentOrderDetailBinding
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModel
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModelFactory
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class OrderDetailFragment : Fragment(){

    private lateinit var binding: FragmentOrderDetailBinding
    private lateinit var viewModel: DetailViewModel
    private val orderViewModel: ListOfOrderViewModel by activityViewModels() {
        val application = requireNotNull(this.activity).application
        val dataSource = OrderDatabase.getInstance(application).orderDatabaseDao

        ListOfOrderViewModelFactory(dataSource, application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_detail, container, false
        )
        //setup the db connection
        val application = requireNotNull(this.activity).application
        val dataSource = OrderDatabase.getInstance(application).orderDatabaseDao

        //viewmodel
        val viewModelFactory = DetailViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(DetailViewModel::class.java)

        //databinding
        binding.detailViewModel = viewModel

        viewModel.setOrders()

        val orderNr = arguments?.getString("OrderNr")!!

        viewModel.findOrder(orderNr)

        binding.lifecycleOwner = this

        //binding.orderNrText2.text = getString(R.string.order_nr_titel, viewModel.dataOrder.value?.orderId)
        viewModel.dataOrder.observe(viewLifecycleOwner) { order ->
            for (i in 1..order.orderStatus.toInt()) {
                Log.i("order", i.toString())
                setStatusActive(i)
            }
        }

        viewModel.etaTime.observe(viewLifecycleOwner, Observer {
            setETA()
        })


        binding.apply {

            val summaryFragment = OrderSummaryFragment()

            val logsFragment = OrderLogsFragment()
            logsFragment.arguments = arguments
            val trackAndTraceFragment = OrderTrackAndTraceFragment()
            trackAndTraceFragment.arguments = arguments
            val fragmentActivity = requireActivity()
            val pagerAdapter = PagerAdapter(fragmentActivity)
            viewPager.adapter = pagerAdapter
            // Add the fragments to the PagerAdapter.
            pagerAdapter.addFragment(summaryFragment)
            pagerAdapter.addFragment(logsFragment)
            pagerAdapter.addFragment(trackAndTraceFragment)
        }

        return binding.root
    }

    private fun setStatusActive(i: Int) {
        val container: CardView = when(i){
            1 -> binding.StatusBox1
            2-> binding.StatusBox2
            3-> binding.StatusBox3
            4 -> binding.StatusBox4
            else -> return
        }
        container.setCardBackgroundColor(Color.GREEN)
        val childChild = container.getChildAt(0) as CardView
        val imgV = childChild.getChildAt(0) as ImageView
        imgV.setColorFilter(Color.GREEN)
    }

    private fun setETA(){
        val simpleformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val advancedformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm")
        val basic = "Verwacht op : "
        val dateTime: LocalDateTime
        val statusChanged: LocalDateTime? = viewModel.dataOrder.value?.statusDate!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        when (viewModel.dataOrder.value?.orderStatus) {
            "1"-> if (statusChanged != null) {
                binding.ETA.text = basic + statusChanged.plusDays(3).format(simpleformatter)
            }
            "2" -> if (statusChanged != null) {
                binding.ETA.text = basic + statusChanged.plusDays(1).format(simpleformatter)
            }
            "3" -> binding.ETA.text = basic + statusChanged?.plusSeconds(viewModel.etaTime.value!!)?.format(advancedformatter)
            "4" -> binding.ETA.text = "Geleverd op : " + statusChanged?.format(advancedformatter)
            else -> dateTime = LocalDateTime.now()
        }
    }
}