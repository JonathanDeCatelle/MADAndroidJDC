package com.example.delawaretrackandtraceapp.screens.detailsoforders

import android.app.Application
import android.util.Log
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.*
import com.example.delawaretrackandtraceapp.database.notifications.NotificationDatabase
import com.example.delawaretrackandtraceapp.database.orderItems.OrderItemDatabase
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabaseDao
import com.example.delawaretrackandtraceapp.database.orders.asOrder
import com.example.delawaretrackandtraceapp.domain.Notification
import com.example.delawaretrackandtraceapp.domain.Order
import com.example.delawaretrackandtraceapp.domain.Package
import com.example.delawaretrackandtraceapp.repository.NotificationRepository
import com.example.delawaretrackandtraceapp.repository.OrderRepository
import com.example.delawaretrackandtraceapp.screens.listoforder.ListOfOrderViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.*

class DetailViewModel(val database: OrderDatabaseDao, app: Application)  : AndroidViewModel(app) {
    private val _dataOrder = MutableLiveData<Order>()
    val dataOrder: LiveData<Order>
        get() = _dataOrder

    private val _dataOrders = MutableLiveData<List<Order>>()
    val dataOrders: LiveData<List<Order>>
        get() = _dataOrders

    private val _etaTime = MutableLiveData<Long>()
    val etaTime: LiveData<Long>
        get() = _etaTime

    private val db = OrderDatabase.getInstance(app.applicationContext)
    private val dbOrderItems = OrderItemDatabase.getInstance(app.applicationContext)
    private val repository = OrderRepository(db, dbOrderItems)

    private val dbNotification = NotificationDatabase.getInstance(app.applicationContext)
    private val notificationRepo = NotificationRepository(dbNotification)

    private val _dataNotifications = MutableLiveData<List<Notification>>()
    val dataNotifications: LiveData<List<Notification>>
        get() = _dataNotifications

    val orders = repository.orders

    init {
        Timber.i("getting orders")
//        viewModelScope.launch {
//            repository.refreshOrders()
//        }
    }

    fun findOrder(nr : String){
        viewModelScope.launch {
            _dataOrder.value = repository.getOrder(nr)
        }
    }

    fun setOrders(){
        _dataOrders.value = orders.value
    }

    fun findNotificationOfOrder(id: String){
        viewModelScope.launch {
            _dataNotifications.value = listOf()
            _dataNotifications.value = notificationRepo.getNotificationsOfOrder(id)
        }
    }

    fun setETATime(totalTimeRoute: Long) {
        _etaTime.value = totalTimeRoute
    }
}