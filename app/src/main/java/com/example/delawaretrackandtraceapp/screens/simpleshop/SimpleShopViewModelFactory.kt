package com.example.delawaretrackandtraceapp.screens.simpleshop

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.delawaretrackandtraceapp.database.products.ProductDatabaseDao

class SimpleShopViewModelFactory(private val dataSource: ProductDatabaseDao, private val application: Application, private val adapter: SimpleShopAdapter): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SimpleShopViewModel::class.java)) {
            return SimpleShopViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}