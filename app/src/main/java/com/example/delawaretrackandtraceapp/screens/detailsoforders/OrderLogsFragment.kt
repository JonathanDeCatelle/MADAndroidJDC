package com.example.delawaretrackandtraceapp.screens.detailsoforders

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
import com.example.delawaretrackandtraceapp.databinding.FragmentOrderLogsBinding
import com.example.delawaretrackandtraceapp.domain.OrderStatus
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModel
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModelFactory
import com.example.delawaretrackandtraceapp.screens.notificationDate
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class OrderLogsFragment : Fragment() {

    private lateinit var binding : FragmentOrderLogsBinding
    private val viewModel: DetailViewModel by activityViewModels()
//    private val orderViewModel: ListOfOrderViewModel by activityViewModels(){
//        val application = requireNotNull(this.activity).application
//        val dataSource = OrderDatabase.getInstance(application).orderDatabaseDao
//
//        ListOfOrderViewModelFactory(dataSource, application)
//        //orderViewModel = ViewModelProvider(requireActivity(), listOfOrderViewModelFactory).get(ListOfOrderViewModel::class.java)
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order_logs, container, false
        )

//        //setup the db connection
//        val application = requireNotNull(this.activity).application
//        val dataSource = OrderDatabase.getInstance(application).orderDatabaseDao
//
//        //viewmodel
//        val viewModelFactory = DetailViewModelFactory(dataSource, application)
//        viewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(DetailViewModel::class.java)

        //databinding
        binding.detailViewModel = viewModel

        viewModel.setOrders()

        val orderNr = arguments?.getString("OrderNr")!!

        viewModel.findOrder(orderNr)
        viewModel.findNotificationOfOrder(orderNr)

        binding.lifecycleOwner = this

        viewModel.dataNotifications.observe(viewLifecycleOwner) { notifications ->
            notifications.forEach {
                val order = viewModel.dataOrder.value!!
                //LINEARLAYOUT
                val log = LinearLayout(context)
                log.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                log.orientation = LinearLayout.HORIZONTAL
                log.gravity = Gravity.CENTER_VERTICAL

                //IMAGE
                val image = ImageView(context)
                image.layoutParams = LinearLayout.LayoutParams(140, 140)
                when (it.message) {
                    "Uw bestelling is geleverd!" -> image.setImageResource(R.drawable.log_home)
                    "De bezorger is onderweg" -> image.setImageResource(R.drawable.log_delivery_truck)
                    else -> image.setImageResource(R.drawable.log_checkmark)
                }
                image.setColorFilter(Color.GREEN)
                image.setPadding(10, 10, 20, 10)


                val dateFormatter = SimpleDateFormat("dd-MM-yyyy | HH:mm ", Locale.getDefault())
                //TEXT
                val text = TextView(context)
                text.text = "${it.message}\n${dateFormatter.format(it.notificationDate)}"
                text.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                text.textSize = 17F

                log.addView(image)
                log.addView(text)
                binding.logs.addView(log)
            }
        }

        return binding.root
    }

//    private fun setLogs(){
//
//        viewModel.dataOrder.observe(this) { order ->
//            order?.notifications?.forEach {
//                //LINEARLAYOUT
//                val log = LinearLayout(context)
//                log.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
//                log.orientation = LinearLayout.HORIZONTAL
//                log.gravity = Gravity.CENTER_VERTICAL
//
//                //IMAGE
//                val image = ImageView(context)
//                image.layoutParams = LinearLayout.LayoutParams(140, 140)
////            if (it.notificationStatus == OrderStatus.DELIVERED) {
////                image.setImageResource(R.drawable.log_home)
////            }else if (it.notificationStatus == OrderStatus.SHIPPED){
////                image.setImageResource(R.drawable.log_delivery_truck)
////            }else{
//                image.setImageResource(R.drawable.log_checkmark)
////            }
//                image.setColorFilter(Color.GREEN)
//                image.setPadding(10, 10, 20, 10)
//
//
//                val formatter = DateTimeFormatter.ofPattern("dd-MM | HH:mm")
//                //TEXT
//                val text = TextView(context)
//                text.text = "${it.message}\n${order.orderDate}"
//                text.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//                text.textSize = 17F
//
//                log.addView(image)
//                log.addView(text)
//                binding.logs.addView(log)
//            }
//        }
//
//    }

}