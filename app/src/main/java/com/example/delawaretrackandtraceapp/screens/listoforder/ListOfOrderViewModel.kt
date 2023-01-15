package com.example.delawaretrackandtraceapp.screens.listoforder

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.delawaretrackandtraceapp.database.orderItems.OrderItemDatabase
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabaseDao
import com.example.delawaretrackandtraceapp.domain.Order
import com.example.delawaretrackandtraceapp.domain.OrderItem
import com.example.delawaretrackandtraceapp.domain.OrderPost
import com.example.delawaretrackandtraceapp.repository.OrderRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber
import com.example.delawaretrackandtraceapp.domain.Package

enum class OrderApiStatus { LOADING, ERROR, DONE }

class ListOfOrderViewModel(val database: OrderDatabaseDao, app: Application) : AndroidViewModel(app) {
    private val _dataOrders = MutableLiveData<List<Order>>()
    val dataOrders: LiveData<List<Order>>
        get() = _dataOrders

    private val _status = MutableLiveData<OrderApiStatus>()
    val status: LiveData<OrderApiStatus>
        get() = _status

    private val _filterChanged = MutableLiveData<Boolean>()
    val filterChanged: LiveData<Boolean>
        get() = _filterChanged

    private val db = OrderDatabase.getInstance(app.applicationContext)
    private val dbOrderItems = OrderItemDatabase.getInstance(app.applicationContext)
    private val repository = OrderRepository(db, dbOrderItems)

    val orders = repository.orders

    init {
        Timber.i("getting orders")
        Log.d("API", "Getting orders - LOADING")
        viewModelScope.launch {
            _status.value = OrderApiStatus.LOADING
            repository.refreshOrders()
            _status.value = OrderApiStatus.DONE
        }
        Log.d("API", "Getting orders - DONE")
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    fun addOrder(order: OrderPost, orderItems: List<OrderItem>, packageApi: Package) {
        viewModelScope.launch {
            _status.value = OrderApiStatus.LOADING
            saveOrderWithRepository(order, orderItems, packageApi)
            _status.value = OrderApiStatus.DONE
        }
    }

    suspend fun saveOrderWithRepository(newOrder: OrderPost, orderItems: List<OrderItem>, packageApi: Package) {
        repository.createOrder(newOrder, orderItems, packageApi)
    }

    fun setFilterChanged() {
        _filterChanged.value = true
    }

    fun setFilterChangedBack() {
        _filterChanged.value = false
    }
}