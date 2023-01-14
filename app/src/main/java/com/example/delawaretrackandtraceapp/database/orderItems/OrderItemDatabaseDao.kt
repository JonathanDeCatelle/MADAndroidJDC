package com.example.delawaretrackandtraceapp.database.orderItems

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

@Dao
interface OrderItemDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg orderItems: DatabaseOrderItem)

    @Insert
    suspend fun insert(orderItem: DatabaseOrderItem)
}