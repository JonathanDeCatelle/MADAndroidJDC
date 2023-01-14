package com.example.delawaretrackandtraceapp.screens.simpleshop

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.delawaretrackandtraceapp.database.products.ProductDatabase
import com.example.delawaretrackandtraceapp.database.products.ProductDatabaseDao
import com.example.delawaretrackandtraceapp.domain.Product
import com.example.delawaretrackandtraceapp.repository.ProductRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import timber.log.Timber

enum class ProductApiStatus { LOADING, ERROR, DONE }

class SimpleShopViewModel(val database: ProductDatabaseDao, app: Application) : AndroidViewModel(app) {
    // ALLES VOOR DE PRODUCTEN IN DE SHOP TE KRIJGEN
    private val _status = MutableLiveData<ProductApiStatus>()
    val status: LiveData<ProductApiStatus>
        get() = _status

    val db = ProductDatabase.getInstance(app.applicationContext)
    val repository = ProductRepository(db)

    val products = repository.products

    init {
        Timber.i("getting jokes")
        Log.d("API", "Getting jokes")
        viewModelScope.launch {
            _status.value = ProductApiStatus.LOADING
            repository.refreshProducts()
            _status.value = ProductApiStatus.DONE
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    // ALLES VOOR DE WINKELWAGEN
    private val _cart = MutableLiveData<List<Product>>()
    val cart: LiveData<List<Product>>
        get() = _cart

//    TODO: Winkelwagen aanpassen aan domain.Product
    fun addToCart(item: Product){
        var alInWinkelwagen: Boolean = false

        _cart.value?.forEach {
            if (it.productName == item.productName) {
                it.aantalInCart += item.aantal
                alInWinkelwagen = true
            }
        }

        if(!alInWinkelwagen){
            item.aantalInCart = item.aantal
            item.aantal = 0
            val liveDataProducts =
                if (_cart.value == null) {
                    Log.d("CART", "Hier bij if " + item.productName)
                    ArrayList<Product>()
                }
                else {
                    ArrayList(_cart.value!!)
                }
            liveDataProducts.add(item)
            _cart.value = liveDataProducts
        }
    }

    fun calculateOffWholeCart(): Double{
        var returnValue: Double = 0.00
        _cart.value?.forEach {
            returnValue += it.aantalInCart * it.unitPrice
        }
        return returnValue
    }

    fun getFirstItemOfCart(): Product? {
        return _cart.value?.get(0) ?: null
    }

    fun resetCart(){
        _cart.value = ArrayList<Product>()
    }
}