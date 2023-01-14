package com.example.delawaretrackandtraceapp.network

import com.example.delawaretrackandtraceapp.database.notifications.DatabaseNotification
import com.example.delawaretrackandtraceapp.domain.Notification
import com.example.delawaretrackandtraceapp.domain.Order
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*

data class ApiNotificationContainer(
    @Json(name = "notifications")
    val apiNotifications: List<ApiNotification>
)

data class ApiNotificationsOrderContainer(
    @Json(name = "notificationsOrder")
    val notificationsOrder: List<ApiNotification>
)

@JsonClass(generateAdapter = true)
data class ApiNotification(
    @Json(name = "notificationId")
    var notificationId: String = "",

    @Json(name = "message")
    var message: String = "",

    @Json(name = "isSeen")
    var isSeen: Int = 0,

    @Json(name = "duration")
    var duration: Int = 0,

    @Json(name = "notificationDate")
    var notificationDate: Date,

    @Json(name = "orderId")
    var orderId: String = "",

)

fun ApiNotificationContainer.asDomainModel(): List<Notification>{
    return apiNotifications.map {
        Notification(
            notificationId = it.notificationId,
            message = it.message,
            isSeen = it.isSeen,
            duration = it.duration,
            notificationDate = it.notificationDate,
            orderId = it.orderId,
            //order = it.order
        )
    }
}

fun ApiNotificationContainer.asDatabaseModel(): Array<DatabaseNotification>{
    return apiNotifications.map {
        DatabaseNotification(
            notificationId = it.notificationId,
            message = it.message,
            isSeen = it.isSeen,
            duration = it.duration,
            notificationDate = it.notificationDate,
            orderId = it.orderId,
        )
    }.toTypedArray()
}

fun ApiNotificationsOrderContainer.asDomainModel(): List<Notification>{
    return notificationsOrder.map {
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

fun ApiNotificationsOrderContainer.asDatabaseModel(): Array<DatabaseNotification>{
    return notificationsOrder.map {
        DatabaseNotification(
            notificationId = it.notificationId,
            message = it.message,
            isSeen = it.isSeen,
            duration = it.duration,
            notificationDate = it.notificationDate,
            orderId = it.orderId,
        )
    }.toTypedArray()
}

fun ApiNotification.asDatabaseNotification(): DatabaseNotification{
    return DatabaseNotification(
        notificationId = notificationId,
        message = message,
        orderId = orderId,
        notificationDate = notificationDate,
        duration = duration,
        isSeen = isSeen,
        )
}