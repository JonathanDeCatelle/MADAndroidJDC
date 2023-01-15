package com.example.delawaretrackandtraceapp.screens.console

import android.app.Application
import androidx.lifecycle.*
import com.example.delawaretrackandtraceapp.database.orderItems.OrderItemDatabase
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabaseDao
import com.example.delawaretrackandtraceapp.domain.Order
import com.example.delawaretrackandtraceapp.repository.OrderRepository
import com.example.delawaretrackandtraceapp.screens.listoforder.OrderApiStatus
import kotlinx.coroutines.launch
import com.example.delawaretrackandtraceapp.domain.Package

enum class OrderApiStatus { LOADING, ERROR, DONE }

class ConsoleViewModel(val database: OrderDatabaseDao, app: Application) : AndroidViewModel(app) {
    private val _status = MutableLiveData<OrderApiStatus>()
    val status: LiveData<OrderApiStatus>
        get() = _status

    private val db = OrderDatabase.getInstance(app.applicationContext)
    private val dbOrderItems = OrderItemDatabase.getInstance(app.applicationContext)
    private val repository = OrderRepository(db, dbOrderItems)

    fun changeStatus(order: Order, packageApi: Package){
        viewModelScope.launch {
            order.orderStatus = (order.orderStatus.toInt() + 1).toString()
            putOrder(order, packageApi)
        }
    }

    suspend fun putOrder(order: Order, packageApi: Package){
        repository.updateOrder(order, packageApi)
    }
}