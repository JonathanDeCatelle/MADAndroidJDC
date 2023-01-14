package com.example.delawaretrackandtraceapp.screens.detailsoforders

import android.R.attr.order
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
import com.example.delawaretrackandtraceapp.databinding.FragmentDetailsOfOrderBinding
import com.example.delawaretrackandtraceapp.domain.OrderStatus
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModel
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModelFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class DetailOfOrderFragment : Fragment() {
    private lateinit var binding: FragmentDetailsOfOrderBinding
    private lateinit var viewModel: DetailViewModel
    private val orderViewModel: ListOfOrderViewModel by activityViewModels() {
        val application = requireNotNull(this.activity).application
        val dataSource = OrderDatabase.getInstance(application).orderDatabaseDao

        ListOfOrderViewModelFactory(dataSource, application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_details_of_order, container, false
        )

        //setup the db connection
        val application = requireNotNull(this.activity).application
        val dataSource = OrderDatabase.getInstance(application).orderDatabaseDao

        //viewmodel
        val viewModelFactory = DetailViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[DetailViewModel::class.java]

        //databinding
        binding.detailViewModel = viewModel

        viewModel.setOrders()

        viewModel.findOrder(arguments?.getString("OrderNr")!!)

        binding.lifecycleOwner = this


        viewModel.dataOrder.observe(viewLifecycleOwner) { order ->
            resetStatusActive()
            for (i in 1..order.orderStatus.toInt()) {
                Log.i("order", i.toString())
                setStatusActive(i)
            }
        }

        setLogs()
        setETA()

        return binding.root
    }

    private fun resetStatusActive() {
        for (i in 1..4) {
            val container: CardView = when(i){
                1 -> binding.StatusBox1
                2-> binding.StatusBox2
                3-> binding.StatusBox3
                4 -> binding.StatusBox4
                else -> return
            }
            container.setCardBackgroundColor(Color.BLACK)
            val childChild = container.getChildAt(0) as CardView
            val imgV = childChild.getChildAt(0) as ImageView
            imgV.setColorFilter(Color.BLACK)
        }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setETA(){
        val simpleformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val advancedformatter = DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm")
        val basic = "Verwacht op : "
        val dateTime: LocalDateTime
        val statusChanged: LocalDateTime? = viewModel.dataOrder.value?.orderDate!!.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        when (viewModel.dataOrder.value?.orderStatus?.toInt()) {
            0-> if (statusChanged != null) {
                binding.ETA.text = basic + statusChanged.plusDays(3).format(simpleformatter)
            }
            1 -> if (statusChanged != null) {
                binding.ETA.text = basic + statusChanged.plusDays(1).format(simpleformatter)
            }
            2 -> binding.ETA.text = basic + estamateArrivalTime(order).format(advancedformatter)
            3 -> binding.ETA.text = "Geleverd op : " + LocalDateTime.now().format(advancedformatter)
            else -> dateTime = LocalDateTime.now()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun estamateArrivalTime(status: Int): LocalDateTime {
                return LocalDateTime.now().plusHours(4)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLogs(){
        binding.logMessages.removeViews(1, binding.logMessages.childCount - 1)

        val order = viewModel.dataOrder.value
        order?.notifications?.forEach {
            //LINEARLAYOUT
            val log = LinearLayout(context)
            log.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            log.orientation = LinearLayout.HORIZONTAL
            log.gravity = Gravity.CENTER_VERTICAL

            //IMAGE
            val image = ImageView(context)
            image.layoutParams = LinearLayout.LayoutParams(140, 140)

                image.setImageResource(R.drawable.log_checkmark)

            image.setColorFilter(Color.GREEN)
            image.setPadding(10, 10, 20, 10)


            val formatter = DateTimeFormatter.ofPattern("dd-MM | HH:mm")

            val text = TextView(context)
            text.text = "${it.message}\n${order.orderDate}"
            text.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            text.textSize = 17F

            log.addView(image)
            log.addView(text)
            binding.logMessages.addView(log)
        }
    }
}