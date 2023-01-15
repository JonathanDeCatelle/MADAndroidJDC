package com.example.delawaretrackandtraceapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.delawaretrackandtraceapp.database.notifications.NotificationDatabase
import com.example.delawaretrackandtraceapp.database.notifications.asDomainModel
import com.example.delawaretrackandtraceapp.database.notifications.asNotification
import com.example.delawaretrackandtraceapp.domain.Notification
import com.example.delawaretrackandtraceapp.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NotificationRepository(private val database: NotificationDatabase) {
    var notifications : LiveData<List<Notification>> = Transformations.map(database.notificationDatabaseDao.getAllUnSeenNofiticationsLive()){
        it.asDomainModel()
    }
    var notificationsAll : LiveData<List<Notification>> = Transformations.map(database.notificationDatabaseDao.getAllNofiticationsLive()){
        it.asDomainModel()
    }

    // Database call
    suspend fun refreshNotifications(){
        //switch context to IO thread
        withContext(Dispatchers.IO){
            val notifications = DelawareApi.retrofitService.getNotifications().await()

            database.notificationDatabaseDao.deleteNotifications()
            database.notificationDatabaseDao.insertAll(* notifications.asDatabaseModel())
        }
    }

    suspend fun updateNotification(notification: Notification){
            //create a Data Transfer Object (Dto)
            val newApiNotification = ApiNotification(
                notificationId = notification.notificationId,
                message = notification.message,
                isSeen = 1,
                duration = notification.duration,
                notificationDate = notification.notificationDate,
                orderId = notification.orderId
            )

            database.notificationDatabaseDao.update(newApiNotification.asDatabaseNotification())
            //TODO update in db
            //DelawareApi.retrofitService.putNotification(newApiNotification).await()
    }

    suspend fun deleteNotificationById(id: String){
        //switch context to IO thread
        withContext(Dispatchers.IO){
            DelawareApi.retrofitService.deleteNotificationByIdAsync(id).await()
        }
    }

    suspend fun getNotificationsOfOrder(id: String): List<Notification>{
        var notifications: List<Notification> = listOf()
        withContext(Dispatchers.IO){
            val notificationsApi = DelawareApi.retrofitService.getNotificationsOfAnOrder(id).await()

            notificationsApi.notificationsOrder.forEach { n ->
               var newNotification = n.asDatabaseNotification().asNotification()

                notifications += newNotification
            }
        }

        return notifications
    }
}