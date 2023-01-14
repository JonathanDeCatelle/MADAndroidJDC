package com.example.delawaretrackandtraceapp.screens.listoforder

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
import com.example.delawaretrackandtraceapp.databinding.FragmentListOfOrderBinding
import com.example.delawaretrackandtraceapp.domain.Order
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.delay
import java.time.Instant.ofEpochMilli
import java.time.ZoneId
import java.util.Date

class ListOfOrderFragment : Fragment() {
    private lateinit var binding: FragmentListOfOrderBinding
    private lateinit var viewModel: ListOfOrderViewModel //by activityViewModels()
    private var orderNmbr: String = ""
    private var firstDate : Date = Date(9999, 12, 30)
    private var secondDate : Date = Date(9999, 12, 30)
    private var sortedAsc = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_list_of_order, container, false)

        //setup the db connection
        val application = requireNotNull(this.activity).application
        val dataSource = OrderDatabase.getInstance(application).orderDatabaseDao

        val viewModelFactory = ListOfOrderViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[ListOfOrderViewModel::class.java]

        val adapter = OrderListAdapter(OrderListener {
            val bundle = bundleOf("OrderNr" to it.orderId)
            findNavController()
                .navigate(R.id.action_listOfOrderFragment_to_orderDetailFragment, bundle)
        }, SortListener {
            sortedAsc = !sortedAsc
            viewModel.setFilterChanged()
        })
        binding.ordersList.adapter = adapter

        binding.apply {
            searchBtn.setOnClickListener {
                firstDate = Date(9999, 12, 30)
                secondDate = Date(9999, 12, 30)
                orderNmbr = binding.orderNrText.text.toString()
                viewModel.setFilterChanged()
            }

            listOfOrderViewModel = viewModel
            DateFilterBtn.setTextColor(Color.WHITE)

            DateFilterBtn.setOnClickListener {
                val datePicker = MaterialDatePicker.Builder.dateRangePicker().build()
                datePicker.show(activity?.supportFragmentManager!!, datePicker.toString())
                datePicker.addOnPositiveButtonClickListener {
                    firstDate = Date.from(ofEpochMilli(it.first).atZone(ZoneId.systemDefault()).toInstant())
                    secondDate = Date.from(ofEpochMilli(it.second).atZone(ZoneId.systemDefault()).toInstant())
                    viewModel.setFilterChanged()
                }
            }
        }
        binding.lifecycleOwner = this

        viewModel.filterChanged.observe(viewLifecycleOwner, Observer { bool ->
            if (bool){
                //FILTER OF DATUM
                if (firstDate != Date(9999, 12, 30) && secondDate != Date(9999, 12, 30)){
                    if(sortedAsc){
                        viewModel.orders.value?.let {
                            adapter.addHeaderAndSubmitList(it.filter { order: Order -> (order.orderDate.after(firstDate).and(order.orderDate.before(secondDate)))
                                .or(order.orderDate == firstDate).or(order.orderDate == secondDate)}.sortedBy { order -> order.orderDate })
                        }
                    }else{
                        viewModel.orders.value?.let {
                            adapter.addHeaderAndSubmitList(it.filter { order: Order -> (order.orderDate.after(firstDate).and(order.orderDate.before(secondDate)))
                                .or(order.orderDate == firstDate).or(order.orderDate == secondDate)}.sortedByDescending { order -> order.orderDate })
                        }
                    }
                //FILTER OP ORDERNUMMER
                }else if(orderNmbr != "" || orderNmbr.isNotEmpty()){
                    if (sortedAsc){
                        viewModel.orders.value?.let {
                            adapter.addHeaderAndSubmitList(it.filter { order: Order -> order.orderId.contains(orderNmbr) }.sortedBy { order -> order.orderDate })
                        }
                    }else{
                        viewModel.orders.value?.let {
                            adapter.addHeaderAndSubmitList(it.filter { order: Order -> order.orderId.contains(orderNmbr) }.sortedByDescending { order -> order.orderDate })
                        }
                    }
                //VOLLEDIGE LIJST
                } else{
                    if(sortedAsc){
                        viewModel.orders.value?.let {
                            adapter.addHeaderAndSubmitList(it.sortedBy { order -> order.orderDate })
                        }
                    }else{
                        viewModel.orders.value?.let {
                            adapter.addHeaderAndSubmitList(it.sortedByDescending { order -> order.orderDate })
                        }
                    }
                }
                viewModel.setFilterChangedBack()
            }
        })

        viewModel.orders.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.addHeaderAndSubmitList(it.sortedBy { order -> order.orderDate })
            }
        })

        return binding.root
    }
}