package com.example.delawaretrackandtraceapp.database.orderItems

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.delawaretrackandtraceapp.domain.OrderItem
import com.squareup.moshi.Json

@Entity(tableName = "orderItems")
data class DatabaseOrderItem(
    @PrimaryKey(autoGenerate = false)
    var orderItemId: String = "",

    @ColumnInfo(name = "quantity")
    @Json(name = "quantity")
    var quantity: Int = 0,

    @ColumnInfo(name = "totalPrice")
    @Json(name = "totalPrice")
    var totalPrice: Double = 0.0,

    @ColumnInfo(name = "productId")
    @Json(name = "productId")
    var productId: String = "",

    @ColumnInfo(name = "orderId")
    @Json(name = "orderId")
    var orderId: String = "",
)

fun List<DatabaseOrderItem>.asDomainModel() : List<OrderItem> {
    return map {
        OrderItem(
            orderItemId = it.orderItemId,
            quantity = it.quantity,
            totalPrice = it.totalPrice,
            productId = it.productId,
            orderId = it.orderId
        )
    }
}