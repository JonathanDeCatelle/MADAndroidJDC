package com.example.delawaretrackandtraceapp.network

import com.example.delawaretrackandtraceapp.database.orderItems.DatabaseOrderItem
import com.example.delawaretrackandtraceapp.domain.OrderItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class ApiOrderItemContainer (
    @Json(name = "orderItems")
    val apiOrderItems: List<ApiOrderItem>
)

@JsonClass(generateAdapter = true)
data class ApiOrderItem(
    @Json(name = "orderItemId")
    var orderItemId: String = "",

    @Json(name = "quantity")
    var quantity: Int = 0,

    @Json(name = "totalPrice")
    var totalPrice: Double = 0.0,

    @Json(name = "productId")
    var productId: String = "",

    @Json(name = "orderId")
    var orderId: String = "",
)

fun ApiOrderItemContainer.asDomainModel(): List<OrderItem>{
    return apiOrderItems.map {
        OrderItem(
            orderItemId = it.orderItemId,
            quantity = it.quantity,
            totalPrice = it.totalPrice,
            productId = it.productId,
            orderId = it.orderId
        )
    }
}

fun ApiOrderItemContainer.asDatabaseModel(): Array<DatabaseOrderItem> {
    return apiOrderItems.map {
        DatabaseOrderItem(
            orderItemId = it.orderItemId,
            quantity = it.quantity,
            totalPrice = it.totalPrice,
            productId = it.productId,
            orderId = it.orderId
        )
    }.toTypedArray()
}

fun ApiOrderItem.asDatabaseModel() : DatabaseOrderItem {
    return DatabaseOrderItem(
        orderItemId = orderItemId,
        quantity = quantity,
        totalPrice = totalPrice,
        productId = productId,
        orderId = orderId
    )
}