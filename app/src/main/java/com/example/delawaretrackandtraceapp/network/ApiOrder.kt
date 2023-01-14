package com.example.delawaretrackandtraceapp.network

import com.example.delawaretrackandtraceapp.database.orders.DatabaseOrder
import com.example.delawaretrackandtraceapp.domain.Order
import com.example.delawaretrackandtraceapp.domain.OrderItem
import com.example.delawaretrackandtraceapp.domain.OrderPost
import com.squareup.moshi.Json
import java.util.*
import com.example.delawaretrackandtraceapp.domain.Package
import com.squareup.moshi.JsonClass

data class ApiOrderContainer (
    @Json(name = "orders")
    val apiOrders: List<ApiOrder>
)

data class OneApiOrderContainer(
    @Json(name = "order")
    val oneApiOrder: ApiOrder
)

data class OneApiOrderPostContainer(
    @Json(name = "order")
    val oneApiOrderPost: ApiOrderPost
)

data class OneApiOrderPutContainer(
    @Json(name = "order")
    val oneApiOrderPut: ApiOrderPut
)


@JsonClass(generateAdapter = true)
data class ApiOrder(
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

    @Json(name = "packageId")
    var packageId: String = "",

    @Json(name = "taxAmount")
    var taxAmount: Int = 0,

    @Json(name = "totalAmount")
    var totalAmount: Double = 0.0,

    @Json(name = "package")
    var packageApi: Package,
)

data class ApiOrderPost(
    @Json(name = "order")
    var order: OrderPost,

    @Json(name = "package")
    var packageApi: Package,

    @Json(name = "items")
    var listOrderItems: List<OrderItem>
)

data class ApiOrderPut(
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

    @Json(name = "invoiceAdress")
    var invoiceAdress: String = "",

    @Json(name = "userId")
    var userId: String = "",

    @Json(name = "taxAmount")
    var taxAmount: Int = 0,

    @Json(name = "totalAmount")
    var totalAmount: Double = 0.0,
)





fun ApiOrderContainer.asDomainModel(): List<Order>{
    return apiOrders.map {
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
            packageApi = it.packageApi,
        )
    }
}

fun ApiOrderContainer.asDatabaseModel(): Array<DatabaseOrder>{
    return apiOrders.map {
        DatabaseOrder(
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
    }.toTypedArray()
}

fun OneApiOrderContainer.asDatabaseOrder(): DatabaseOrder{
    return DatabaseOrder(
        orderId = oneApiOrder.orderId,
        netPrice = oneApiOrder.netPrice,
        orderDate = oneApiOrder.orderDate,
        statusDate = oneApiOrder.statusDate,
        orderStatus = oneApiOrder.orderStatus,
        deliveryAdress = oneApiOrder.deliveryAdress,
        invoiceAdress = oneApiOrder.invoiceAdress,
        userId = oneApiOrder.userId,
        packageId = oneApiOrder.packageId,
        taxAmount = oneApiOrder.taxAmount,
        totalAmount = oneApiOrder.totalAmount,
        packageApi = oneApiOrder.packageApi
    )
}

fun ApiOrder.asDatabaseOrder(): DatabaseOrder{
    return DatabaseOrder(
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