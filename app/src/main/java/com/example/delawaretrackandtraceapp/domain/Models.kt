package com.example.delawaretrackandtraceapp.domain

import java.util.Date
import com.example.delawaretrackandtraceapp.domain.Package
import com.squareup.moshi.Json

data class Product(
    var productId: String = "",
    var productCategoryId: Int = 0,
    var productName: String = "",
    var unitPrice: Double = 0.0,
    var supplierName: String = "",
    var lefInStock: Int = 0,
    var description: String = "",
    var height: Double = 0.0,
    var width: Double = 0.0,
    var depth: Double = 0.0,
) {
    var aantalInCart: Int = 0
    var aantal: Int = 0
}

data class Order(
    var orderId: String = "",
    var netPrice: Double = 0.0,
    var orderDate: Date,
    var statusDate: Date,
    var orderStatus: String = "",
    var deliveryAdress: String = "",
    var invoiceAdress: String = "",
    var userId: String = "",
    var packageId: String = "",
    var taxAmount: Int = 0,
    var totalAmount: Double = 0.0,
    var packageApi: Package
) {
    val items: List<Item> = mutableListOf()
    val notifications: List<Notification> = mutableListOf()

    fun nextStatus() {
        if (orderStatus.toInt() in 0..2) {
            this.orderStatus = OrderStatus.values()[orderStatus.toInt().inc()].toString()
        }
    }
    fun previousStatus() {
        if (this.orderStatus.toInt() in 1..4) {
            this.orderStatus = OrderStatus.values()[orderStatus.toInt().dec()].toString()
        }
    }
}

data class OrderPost(
    var orderId: String = "",
    var netPrice: Double = 0.0,
    var orderDate: Date,
    var statusDate: Date,
    var orderStatus: String = "",
    var deliveryAdress: String = "",
    var invoiceAdress: String = "",
    var userId: String = "",
    var packageId: String = "",
    var taxAmount: Int = 0,
    var totalAmount: Double = 0.0,
)

data class OrderPut(
    @Json(name = "orderId")
    var orderId: String = "",

    @Json(name = "netPrice")
    var netPrice: Double = 0.0,

    @Json(name = "orderDate")
    var orderDate: Date,

    @Json(name = "statusDate")
    var statusDate: Date,

    @Json(name = "orderStatus")
    var orderStatus: String = "",

    @Json(name = "deliveryAdress")
    var deliveryAdress: String = "",

    @Json(name = "invoiceAddress")
    var invoiceAdress: String = "",

    @Json(name = "userId")
    var userId: String = "",

    @Json(name = "taxAmount")
    var taxAmount: Int = 0,

    @Json(name = "totalAmount")
    var totalAmount: Double = 0.0,
)

data class Notification(
    var notificationId: String = "",
    var message: String = "",
    var isSeen: Int = 0,
    var duration: Int = 0,
    var notificationDate: Date,
    var orderId: String = "",
    //var order: Order,
)

data class Package(
    var packageId: String = "",
    var height: Int = 0,
    var width: Int = 0,
    var dept: Int = 0,
    var price: Int = 0,
)

data class OrderItem (
    var orderItemId: String = "",
    var quantity: Int = 0,
    var totalPrice: Double = 0.0,
    var productId: String = "",
    var orderId: String = "",
) {
    override fun toString(): String {
        return "Product: " /*+ this.product.productName*/ + "\tAantal: " + this.quantity + "\tPrijs: " + this.totalPrice
    }
}