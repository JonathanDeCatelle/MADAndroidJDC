package com.example.delawaretrackandtraceapp.database.packages

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase

@Database(entities = [DatabasePackage::class], version = 1, exportSchema = false)
abstract class PackageDatabase : RoomDatabase() {
    abstract val packageDatabaseDao: PackageDatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: PackageDatabase? = null

        fun getInstance(context: Context): PackageDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        PackageDatabase::class.java,
                        "package_database"
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