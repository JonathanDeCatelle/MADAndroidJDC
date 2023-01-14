package com.example.delawaretrackandtraceapp.database.orders

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.delawaretrackandtraceapp.database.DateTypeConverter
import com.example.delawaretrackandtraceapp.database.PackageTypeConverter
import com.example.delawaretrackandtraceapp.domain.Order
import com.squareup.moshi.Json
import java.util.*
import com.example.delawaretrackandtraceapp.domain.Package

@Entity(tableName = "orders")
@TypeConverters(DateTypeConverter::class, PackageTypeConverter::class)
data class DatabaseOrder(
    @PrimaryKey(autoGenerate = false)
    var orderId: String = "",

    @ColumnInfo(name = "netPrice")
    @Json(name = "netPrice")
    var netPrice: Double = 0.0,

    @ColumnInfo(name = "orderDate")
    @Json(name = "orderDate")
    var orderDate: Date,

    @ColumnInfo(name = "statusDate")
    @Json(name = "statusDate")
    var statusDate: Date,

    @ColumnInfo(name = "orderStatus")
    @Json(name = "orderStatus")
    var orderStatus: String = "",

    @ColumnInfo(name = "deliveryAddress")
    @Json(name = "deliveryAddress")
    var deliveryAdress: String = "",

    @ColumnInfo(name = "invoiceAddress")
    @Json(name = "invoiceAddress")
    var invoiceAdress: String = "",

    @ColumnInfo(name = "userId")
    @Json(name = "userId")
    var userId: String = "",

    @ColumnInfo(name = "packageId")
    @Json(name = "packageId")
    var packageId: String = "",

    @ColumnInfo(name = "taxAmount")
    @Json(name = "taxAmount")
    var taxAmount: Int = 0,

    @ColumnInfo(name = "totalAmount")
    @Json(name = "totalAmount")
    var totalAmount: Double = 0.0,

    @ColumnInfo(name = "package")
    @Json(name = "package")
    var packageApi: Package
)

fun List<DatabaseOrder>.asDomainModel() : List<Order>{
    return map {
        Order(
            orderId = it.orderId,
            netPrice = it.netPrice,
            orderDate = it.orderDate,
            statusDate = it.statusDate,
            orderStatus = it.orderStatus,
            deliveryAdress = it.deliveryAdress,
            invoiceAdress = it.invoiceAdress,
            userId = it.userId,
            packageId = it.packageId,
            taxAmount = it.taxAmount,
            totalAmount = it.totalAmount,
            packageApi = it.packageApi
        )
    }
}

fun DatabaseOrder.asOrder(): Order{
    return Order(
        orderId = orderId,
        netPrice = netPrice,
        orderDate = orderDate,
        statusDate = statusDate,
        orderStatus = orderStatus,
        deliveryAdress = deliveryAdress,
        invoiceAdress = invoiceAdress,
        userId = userId,
        packageId = packageId,
        taxAmount = taxAmount,
        totalAmount = totalAmount,
        packageApi = packageApi
    )
}