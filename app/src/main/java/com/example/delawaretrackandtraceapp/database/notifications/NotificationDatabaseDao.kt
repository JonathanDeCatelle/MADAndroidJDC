package com.example.delawaretrackandtraceapp.database.notifications

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotificationDatabaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg notifications : DatabaseNotification)

    @Query("SELECT * FROM notifications ORDER BY notificationDate DESC")
    fun getAllNofiticationsLive(): LiveData<List<DatabaseNotification>>

    @Query("SELECT * FROM notifications WHERE isSeen = 0 ORDER BY notificationDate DESC")
    fun getAllUnSeenNofiticationsLive(): LiveData<List<DatabaseNotification>>

    @Query("DELETE FROM notifications WHERE notificationId = :notificationId")
    fun deleteByNotificationId(notificationId: String)

    @Query("DELETE FROM notifications")
    fun deleteNotifications()

    @Update
    suspend fun update(notification: DatabaseNotification)
}