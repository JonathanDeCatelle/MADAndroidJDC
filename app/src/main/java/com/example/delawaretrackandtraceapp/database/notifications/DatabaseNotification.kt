package com.example.delawaretrackandtraceapp.database.notifications

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.delawaretrackandtraceapp.database.DateTypeConverter
import com.example.delawaretrackandtraceapp.database.OrderTypeConverter
import com.example.delawaretrackandtraceapp.domain.Notification
import com.example.delawaretrackandtraceapp.domain.Order
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "notifications")
@TypeConverters(DateTypeConverter::class, OrderTypeConverter::class)
data class DatabaseNotification(
    @PrimaryKey(autoGenerate = false)
    var notificationId: String = "",

    @ColumnInfo(name = "message")
    @Json(name = "message")
    var message: String = "",

    @ColumnInfo(name = "isSeen")
    @Json(name = "isSeen")
    var isSeen: Int = 0,

    @ColumnInfo(name = "duration")
    @Json(name = "duration")
    var duration: Int = 0,

    @ColumnInfo(name = "notificationDate")
    @Json(name = "notificationDate")
    var notificationDate: Date,

    @ColumnInfo(name = "orderId")
    @Json(name = "orderId")
    var orderId: String = "",

)

fun List<DatabaseNotification>.asDomainModel(): List<Notification>{
    return map {
        Notification(
            notificationId = it.notificationId,
            message = it.message,
            isSeen = it.isSeen,
            duration = it.duration,
            notificationDate = it.notificationDate,
            orderId = it.orderId,
        )
    }
}

fun DatabaseNotification.asNotification(): Notification {
    return Notification(
        notificationId = notificationId,
        message = message,
        isSeen = isSeen,
        duration = duration,
        notificationDate = notificationDate,
        orderId = orderId,
    )
}