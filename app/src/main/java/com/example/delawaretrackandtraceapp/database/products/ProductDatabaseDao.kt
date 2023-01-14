package com.example.delawaretrackandtraceapp.database.products

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ProductDatabaseDao {

    //adding insert all with varar
    //replace strategy: upsert
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg products : DatabaseProduct)

    @Query("SELECT * FROM products ORDER BY productId DESC")
    fun getAllProductsLive(): LiveData<List<DatabaseProduct>>
}