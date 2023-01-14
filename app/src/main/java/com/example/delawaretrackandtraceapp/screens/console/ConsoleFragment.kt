package com.example.delawaretrackandtraceapp.screens.console


import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDeepLinkBuilder
import com.example.delawaretrackandtraceapp.MainActivity
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
import com.example.delawaretrackandtraceapp.databinding.FragmentConsoleBinding
import com.example.delawaretrackandtraceapp.domain.Order
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModel
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModelFactory


class ConsoleFragment : Fragment(){

    private lateinit var viewModel: ConsoleViewModel
    private val orderViewModel: ListOfOrderViewModel by activityViewModels(){
        val application = requireNotNull(this.activity).application
        val dataSource = OrderDatabase.getInstance(application).orderDatabaseDao

        ListOfOrderViewModelFactory(dataSource, application)
        //orderViewModel = ViewModelProvider(requireActivity(), listOfOrderViewModelFactory).get(ListOfOrderViewModel::class.java)
    }

    private lateinit var binding : FragmentConsoleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_console,
            container,
            false)

        createNotificationChannel()

        //setup the db connection
        val application = requireNotNull(this.activity).application
        val dataSource = OrderDatabase.getInstance(application).orderDatabaseDao

        val viewModelFactory = ConsoleViewModelFactory(dataSource, application)
        viewModel = ViewModelProvider(requireActivity(), viewModelFactory)[ConsoleViewModel::class.java]
        binding.consoleViewModel = viewModel
        binding.lifecycleOwner = this

        orderViewModel.orders.observe(viewLifecycleOwner, Observer {
            showOrders(it.sortedBy { order -> order.orderId })
        })

        return binding.root
    }

    private fun showOrders(orders: List<Order>) {
        binding.ordersLayout.removeAllViews()
        var odd = false
        orders.forEach {
            val order = LinearLayout(context)
            order.layoutParams =
                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1F)
            order.orientation = LinearLayout.HORIZONTAL
            odd = if (!odd) {
                order.setBackgroundColor(Color.parseColor("#EFF7FF"))
                true
            } else {
                false
            }
            order.setPadding(0, 10, 0, 10)


            val orderNr = TextView(context)
            orderNr.text = it.orderId
            orderNr.setTextAppearance(R.style.orderList_style)
            orderNr.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.33F)
            orderNr.gravity = Gravity.CENTER

            val buttonSatusUp = Button(context)
            buttonSatusUp.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 0.13F)
            buttonSatusUp.setBackgroundColor(Color.TRANSPARENT)
            buttonSatusUp.text = ">"

            buttonSatusUp.setOnClickListener { view: View ->

                viewModel.changeStatus(it, it.packageApi)

                sendNotification(it)


            }


            val orderStatus = TextView(context)
            orderStatus.text = it.orderStatus
            orderStatus.setTextAppearance(R.style.orderList_style)
            orderStatus.layoutParams =
                LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 0.33F)
            orderStatus.gravity = Gravity.CENTER

            order.addView(orderNr)

            order.addView(buttonSatusUp)
            order.addView(orderStatus)
            binding.ordersLayout.addView(order)

        }
    }

    //NOTIFICATIONS
    private val CHANNEL_ID = "channel_notifications"
    private val GROUP_KEY_NOTIFICATIONS = "com.android.example.notifications"
    private val notificationId = 101
    private val SUMMARY_ID = 0

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Meldingen"
            val descriptionText = "Notificaties Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(order: Order){
        val bundle = bundleOf("OrderNr" to order.orderId)

        Log.i("Noti", order.orderId)

        val pendingIntent = NavDeepLinkBuilder(requireContext())
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.navigation)
            .setDestination(R.id.orderDetailFragment)
            .setArguments(bundle)
            .createPendingIntent()

        val statusText = checkStatus(order.orderStatus)

        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.delaware_logo_opengraph__1_)
            .setContentTitle(statusText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setGroup(GROUP_KEY_NOTIFICATIONS)
            .setGroupSummary(true)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(requireContext())) {
            notify(SUMMARY_ID, builder.build())
        }
    }

    private fun checkStatus(status: String): String {
        when (status) {
            "1" -> return "Uw bestelling werd succesvol aangemaakt!"
            "2" -> return "Uw bestelling werd verwerkt!"
            "3" -> return "De bezorger is onderweg"
            "4" -> return "Uw bestelling is geleverd!"
            else -> return "onbekende status"
        }
    }
}