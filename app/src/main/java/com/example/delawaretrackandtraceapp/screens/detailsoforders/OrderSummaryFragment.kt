package com.example.delawaretrackandtraceapp.screens.detailsoforders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
//import com.example.delawaretrackandtraceapp.databinding.FragmentMapsBinding
import com.example.delawaretrackandtraceapp.databinding.FragmentOrderSummaryBinding
import com.example.delawaretrackandtraceapp.domain.Order
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModel
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModelFactory
import org.w3c.dom.Text

class OrderSummaryFragment : Fragment() {

    private lateinit var binding: FragmentOrderSummaryBinding
    private val viewModel: DetailViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order_summary, container, false)

        viewModel.dataOrder.observe(viewLifecycleOwner) { order ->
            binding.apply {
                for (item in order!!.items) {
                    var t = TextView(activity)
                    t.text = item.toString()
                    summaryLayout.addView(t)
                }
            }
        }



        return binding.root
    }

}