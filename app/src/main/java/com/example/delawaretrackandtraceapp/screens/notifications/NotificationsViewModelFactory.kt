package com.example.delawaretrackandtraceapp.screens.notifications

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.delawaretrackandtraceapp.database.notifications.NotificationDatabase
import com.example.delawaretrackandtraceapp.database.notifications.NotificationDatabaseDao

class NotificationsViewModelFactory(private val dataSource: NotificationDatabaseDao, private val application: Application): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(NotificationsViewModel::class.java)) {
            return NotificationsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")    }
}