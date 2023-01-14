package com.example.delawaretrackandtraceapp.screens

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.delawaretrackandtraceapp.R
import com.example.delawaretrackandtraceapp.domain.Notification
import com.example.delawaretrackandtraceapp.domain.Order
import com.example.delawaretrackandtraceapp.domain.OrderStatus
import com.example.delawaretrackandtraceapp.domain.Product
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("ProductPrijs")
fun TextView.productPrijsText(p: Product) {
    this.text = p.unitPrice.toString()
}

@BindingAdapter("ProductNaam")
fun TextView.productNaamText(p: Product) {
    this.text = p.productName
}

@BindingAdapter("ProductPlus")
fun ImageView.productPlusImage(p: Product) {
    this.setImageResource(R.drawable.plus)
}

@BindingAdapter("NotificationMessage")
fun TextView.notificationMessage(n: Notification) {
    this.text = resources.getString(R.string.notification_message, n.orderId, n.message)
}

@BindingAdapter("NotificationDate")
fun TextView.notificationDate(n: Notification) {
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy | HH:mm ", Locale.getDefault())
    this.text = dateFormatter.format(n.notificationDate)
}

@BindingAdapter("NotificationImage")
fun ImageView.notificationImage(n: Notification) {
    when(n.message){
        "De bezorger is onderweg" -> this.setImageResource(R.drawable.log_delivery_truck)
        "Uw bestelling is geleverd!" -> this.setImageResource(R.drawable.log_home)
        else -> this.setImageResource(R.drawable.log_checkmark)
    }
}

@BindingAdapter("OrderDate")
fun TextView.orderDate(o: Order) {
    val dateFormatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    this.text = dateFormatter.format(o.orderDate)
}

@BindingAdapter("OrderStatus")
fun TextView.orderStatus(o: Order) {
    when(o.orderStatus){
        "1" -> this.text = OrderStatus.BESTELD.name
        "2" -> this.text = OrderStatus.VERWERKT.name
        "3" -> this.text = OrderStatus.VERZONDEN.name
        "4" -> this.text = OrderStatus.GELEVERD.name
    }
}

