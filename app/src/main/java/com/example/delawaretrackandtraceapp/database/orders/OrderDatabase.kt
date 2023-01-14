package com.example.delawaretrackandtraceapp.database.orders

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.delawaretrackandtraceapp.database.products.DatabaseProduct
import com.example.delawaretrackandtraceapp.database.products.ProductDatabase

@Database(entities = [DatabaseOrder::class], version = 2, exportSchema = false)
abstract class OrderDatabase : RoomDatabase() {
    abstract val orderDatabaseDao: OrderDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: OrderDatabase? = null

        fun getInstance(context: Context): OrderDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        OrderDatabase::class.java,
                        "orders_database"
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