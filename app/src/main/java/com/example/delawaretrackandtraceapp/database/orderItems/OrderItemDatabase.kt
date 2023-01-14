package com.example.delawaretrackandtraceapp.database.orderItems

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase

@Database(entities = [DatabaseOrderItem::class], version = 1, exportSchema = false)
abstract class OrderItemDatabase : RoomDatabase() {
    abstract val orderItemDatabaseDao: OrderItemDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: OrderItemDatabase? = null

        fun getInstance(context: Context): OrderItemDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        OrderItemDatabase::class.java,
                        "orderItems_database"
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