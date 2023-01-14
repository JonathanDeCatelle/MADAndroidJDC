package com.example.delawaretrackandtraceapp.database.notifications

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.delawaretrackandtraceapp.database.products.ProductDatabase

@Database(entities = [DatabaseNotification::class], version = 3, exportSchema = false)
abstract class NotificationDatabase : RoomDatabase() {

    abstract val notificationDatabaseDao: NotificationDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: NotificationDatabase? = null

        fun getInstance(context: Context): NotificationDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotificationDatabase::class.java,
                        "notifications_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}