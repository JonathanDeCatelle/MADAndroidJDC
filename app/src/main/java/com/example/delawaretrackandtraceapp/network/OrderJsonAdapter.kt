package com.example.delawaretrackandtraceapp.network

import com.example.delawaretrackandtraceapp.domain.Order
import com.example.delawaretrackandtraceapp.domain.Package
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonReader
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Date


class OrderJsonAdapter {
    @ToJson
    fun toJson(order: Order): JsonElement {
        val json = JsonObject()
        json.addProperty("orderId", order.orderId)
        json.addProperty("netPrice", order.netPrice)
        json.addProperty("orderDate", order.orderDate.toString())
        json.addProperty("statusDate", order.statusDate.toString())
        json.addProperty("orderStatus", order.orderStatus)
        json.addProperty("deliveryAddress", order.deliveryAdress)
        json.addProperty("invoiceAddress", order.invoiceAdress)
        json.addProperty("userId", order.userId)
        json.addProperty("packageId", order.packageId)
        json.addProperty("taxAmount", order.taxAmount)
        json.addProperty("totalAmount", order.totalAmount)
        json.add("packageApi", toJsonPackage(order.packageApi))
        return json
    }

    @FromJson
    fun fromJson(reader: JsonReader): Order {
        reader.beginObject()
        var orderId = ""
        var netprice = 0.0
        var orderDate = Date()
        var statusDate = Date()
        var orderStatus = ""
        var deliveryAddress = ""
        var invoiceAddress = ""
        var userId = ""
        var packageId = ""
        var taxAmount = 0
        var totalAmount = 0.0
        var packageApi: Package = Package()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "orderId" -> orderId = reader.nextString()
                "netprice" -> netprice = reader.nextDouble()
                "orderDate" -> orderDate = dateFromString(reader.nextString())
                "statusdate" -> statusDate = dateFromString(reader.nextString())
                "orderStatus" -> orderStatus = reader.nextString()
                "deliveryAddress" -> deliveryAddress = reader.nextString()
                "invoiceAddress" -> invoiceAddress = reader.nextString()
                "userId" -> userId = reader.nextString()
                "packageId" -> packageId = reader.nextString()
                "taxAmount" -> taxAmount = reader.nextInt()
                "totalAmount" -> totalAmount = reader.nextDouble()
                "package" -> packageApi = fromJsonPackage(reader)
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return Order(
            orderId,
            netprice,
            orderDate,
            statusDate,
            orderStatus,
            deliveryAddress,
            invoiceAddress,
            userId,
            packageId,
            taxAmount,
            totalAmount,
            packageApi
        )
    }

    fun dateFromString(dateString: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        return dateFormat.parse(dateString)
    }

    fun fromJsonPackage(reader: JsonReader): Package {
        reader.beginObject()
        var packageId = ""
        var height = 0
        var width = 0
        var dept = 0
        var price = 0
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "packageId" -> packageId = reader.nextString()
                "height" -> height = reader.nextInt()
                "width" -> width = reader.nextInt()
                "dept" -> dept = reader.nextInt()
                "price" -> price = reader.nextInt()
                else -> reader.skipValue()
            }
        }
        reader.endObject()
        return Package(packageId, height, width, dept, price)
    }

    fun toJsonPackage(packageApi: Package): JsonElement {
        val json = JsonObject()
        json.addProperty("packageId", packageApi.packageId)
        json.addProperty("height", packageApi.height)
        json.addProperty("width", packageApi.width)
        json.addProperty("dept", packageApi.dept)
        json.addProperty("price", packageApi.price)
        return json
    }

}
