package com.example.delawaretrackandtraceapp.screens.console

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabaseDao

class ConsoleViewModelFactory(private val dataSource: OrderDatabaseDao, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if(modelClass.isAssignableFrom(ConsoleViewModel::class.java)) {
            return ConsoleViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}