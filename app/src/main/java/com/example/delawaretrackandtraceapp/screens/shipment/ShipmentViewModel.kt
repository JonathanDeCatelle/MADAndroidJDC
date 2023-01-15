package com.example.delawaretrackandtraceapp.screens.shipment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.delawaretrackandtraceapp.domain.Order
import com.example.delawaretrackandtraceapp.domain.Product


class ShipmentViewModel : ViewModel() {

    private var _dataOrder = MutableLiveData<Order>()
    val dataOrder: LiveData<Order>
        get() = _dataOrder

    private val _dataOrders = MutableLiveData<List<Order>>()
    val dataOrders: LiveData<List<Order>>
        get() = _dataOrders

    fun findOrder(nr : String){
//        _dataOrder.value = Orders().findOrder(nr)
        _dataOrder.value = _dataOrders.value?.find { o -> o.orderId == nr }!!;
    }

    fun setOrders(orders: List<Order>){
        _dataOrders.value = orders
    }




    init {

    }

}