package com.example.delawaretrackandtraceapp.screens.notifications

import android.app.Application
import androidx.lifecycle.*
import com.example.delawaretrackandtraceapp.database.notifications.NotificationDatabase
import com.example.delawaretrackandtraceapp.database.notifications.NotificationDatabaseDao
import com.example.delawaretrackandtraceapp.domain.Notification
import com.example.delawaretrackandtraceapp.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

enum class NotificationApiStatus { LOADING, ERROR, DONE }

class NotificationsViewModel(val database: NotificationDatabaseDao, app: Application) : AndroidViewModel(app) {
    private val _status = MutableLiveData<NotificationApiStatus>()
    val status: LiveData<NotificationApiStatus>
        get() = _status

    private val _allOrNew = MutableLiveData<Boolean>()
    val allOrNew: LiveData<Boolean>
        get() = _allOrNew

    val db = NotificationDatabase.getInstance(app.applicationContext)
    private val repository = NotificationRepository(db)

    var notificationsAll = repository.notificationsAll
    var notificationsNew = repository.notifications

    init {
        Timber.i("getting notifications")
        viewModelScope.launch {
            _status.value = NotificationApiStatus.LOADING
            repository.refreshNotifications()
            _status.value = NotificationApiStatus.DONE
        }
    }

    fun showNewNotifications(){
        _allOrNew.value = true
    }

    fun showAllNotifications(){
        _allOrNew.value = false
    }

    fun updateNotification(notification: Notification){
        Timber.i("Update notification")
        viewModelScope.launch {
            _status.value = NotificationApiStatus.LOADING
            repository.updateNotification(notification)
            _status.value = NotificationApiStatus.DONE
        }
    }

    fun deleteNotificationById(id:String){
        Timber.i("Delete notification")
        viewModelScope.launch {
            _status.value = NotificationApiStatus.LOADING
            withContext(Dispatchers.IO){
                db.notificationDatabaseDao.deleteByNotificationId(id)
            }
            repository.deleteNotificationById(id)
            _status.value = NotificationApiStatus.DONE
        }
    }
}