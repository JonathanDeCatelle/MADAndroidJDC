package com.example.delawaretrackandtraceapp.database

import androidx.room.TypeConverter
import com.example.delawaretrackandtraceapp.domain.Order
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class OrderTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromOrder(order: Order): String {
        val type = object : TypeToken<Order>() {}.type
        return gson.toJson(order, type)
    }

    @TypeConverter
    fun toOrder(orderString: String): Order {
        val type = object : TypeToken<Order>() {}.type
        return gson.fromJson(orderString, type)
    }
}
