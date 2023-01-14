package com.example.delawaretrackandtraceapp.repository

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.delawaretrackandtraceapp.database.products.ProductDatabase
import com.example.delawaretrackandtraceapp.database.products.asDomainModel
import com.example.delawaretrackandtraceapp.domain.Product
import com.example.delawaretrackandtraceapp.network.DelawareApi
import com.example.delawaretrackandtraceapp.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/*
* Class to connect the db and the network
* Contains a LiveData object with products
* Can refresh products
* */
class ProductRepository(private val database: ProductDatabase) {
    val products = MediatorLiveData<List<Product>>()

    //keep a reference to the original livedata
    private var changeableLiveData = Transformations.map(database.productDatabaseDao.getAllProductsLive()){
        it.asDomainModel()
    }

    //add the data to the mediator
    init {
        products.addSource(
            changeableLiveData
        ){
            products.value = it
        }
    }

    // Database call
    suspend fun refreshProducts(){
        //switch context to IO thread
        withContext(Dispatchers.IO){
            val products = DelawareApi.retrofitService.getProducts().await()
            //'*': kotlin spread operator.
            //Used for functions that expect a vararg param
            //just spreads the array into separate fields
            database.productDatabaseDao.insertAll(*products.asDatabaseModel())
            Log.d("WARNING", "End suspend")
            Timber.i("end suspend")
        }
    }
}