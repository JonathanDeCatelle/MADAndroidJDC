package com.example.delawaretrackandtraceapp.database.orders

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface OrderDatabaseDao {
    @Insert
    suspend fun insert(order: DatabaseOrder)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg orders: DatabaseOrder)

    @Query("SELECT * FROM orders ORDER BY orderId DESC")
    fun getAllOrdersLive(): LiveData<List<DatabaseOrder>>

    @Query("SELECT * FROM orders WHERE orderId = :key")
    suspend fun get(key: String): DatabaseOrder?

    @Query("DELETE FROM orders")
    suspend fun deleteAll()

    @Update
    suspend fun update(order: DatabaseOrder)
}