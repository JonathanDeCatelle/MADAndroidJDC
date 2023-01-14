package com.example.delawaretrackandtraceapp.screens.productOverviewFromAPI

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.delawaretrackandtraceapp.database.products.ProductDatabase
import com.example.delawaretrackandtraceapp.repository.ProductRepository
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import timber.log.Timber

enum class ProductApiStatus { LOADING, ERROR, DONE }

class ProductOverviewFromAPIViewModel(application: Application) : AndroidViewModel(application) {
    private val _status = MutableLiveData<ProductApiStatus>()
    val status: LiveData<ProductApiStatus>
        get() = _status

    private val database = ProductDatabase.getInstance(application.applicationContext)
    private val productRepository = ProductRepository(database)

    val products = productRepository.products

    init {
        Log.d("WARNING", "Getting products")
        Timber.i("getting products")
        viewModelScope.launch {
            _status.value = ProductApiStatus.LOADING
            Log.d("WARNING", "Getting products - LADEN")
            productRepository.refreshProducts()
            Log.d("WARNING", "Getting products - KLAAR")
            _status.value = ProductApiStatus.DONE
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductOverviewFromAPIViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProductOverviewFromAPIViewModel(app) as T
            }
            throw java.lang.IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}