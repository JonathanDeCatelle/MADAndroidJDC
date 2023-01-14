package com.example.delawaretrackandtraceapp.screens.cart

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.delawaretrackandtraceapp.domain.Product

class CartViewModel : ViewModel() {
    private val _cart = MutableLiveData<List<Product>>()
    val cart: LiveData<List<Product>>
        get() = _cart

    fun setCart(list: LiveData<List<Product>>){
        _cart.value = list.value
    }

    fun calculateOffWholeCart(): Double{
        var returnValue: Int = 0
        return 0.0
    }

    fun getFirstItemOfCart(): Product? {
        return _cart.value?.get(0) ?: null
    }

    fun resetCart(){
        _cart.value = ArrayList<Product>()
    }
}