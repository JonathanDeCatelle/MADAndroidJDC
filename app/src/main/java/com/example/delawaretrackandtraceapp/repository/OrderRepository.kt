package com.example.delawaretrackandtraceapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.delawaretrackandtraceapp.database.orderItems.OrderItemDatabase
import com.example.delawaretrackandtraceapp.database.orders.OrderDatabase
import com.example.delawaretrackandtraceapp.database.orders.asDomainModel
import com.example.delawaretrackandtraceapp.database.orders.asOrder
import com.example.delawaretrackandtraceapp.domain.Order
import com.example.delawaretrackandtraceapp.domain.OrderItem
import com.example.delawaretrackandtraceapp.domain.OrderPost
import com.example.delawaretrackandtraceapp.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import com.example.delawaretrackandtraceapp.domain.Package

class OrderRepository(private val database: OrderDatabase, private val orderItemDatabase: OrderItemDatabase) {
    val orders : LiveData<List<Order>> = Transformations.map(database.orderDatabaseDao.getAllOrdersLive()){
        it.asDomainModel()
    }

    // Database call
    suspend fun refreshOrders(){
        //switch context to IO thread
        withContext(Dispatchers.IO){
            val orders = DelawareApi.retrofitService.getOrders().await()
            //'*': kotlin spread operator.
            //Used for functions that expect a vararg param
            //just spreads the array into separate fields
            database.orderDatabaseDao.deleteAll()
            database.orderDatabaseDao.insertAll(* orders.asDatabaseModel())
            Timber.i("end suspend")
        }
    }

    suspend fun getOrder(id: String): Order {
        var order: Order
        //switch context to IO thread
        withContext(Dispatchers.IO){
            val orderApi = DelawareApi.retrofitService.getOrder(id).await()

            order = orderApi.asDatabaseOrder().asOrder()
            //order = database.orderDatabaseDao.get(id)!!.asOrder()
            Timber.i("end suspend")
//            Log.d("iets", "end suspend")
//            delay(500)
        }

        return order
    }

    suspend fun createOrder(newOrder: OrderPost, items: List<OrderItem>, packageApi: Package): OrderPost {
        val newApiOrderPost = ApiOrderPost(
            order = newOrder,
            packageApi = packageApi,
            listOrderItems = items,
        )

        val newApiOrder = ApiOrder(
            orderId = newOrder.orderId,
            netPrice = newOrder.netPrice,
            orderDate = newOrder.orderDate,
            statusDate = newOrder.statusDate,
            orderStatus = newOrder.orderStatus,
            deliveryAdress = newOrder.deliveryAdress,
            invoiceAdress = newOrder.invoiceAdress,
            userId = newOrder.userId,
            packageId = newOrder.packageId,
            taxAmount = newOrder.taxAmount,
            totalAmount = newOrder.totalAmount,
            packageApi = packageApi,
        )

        try {
            Log.i("Order Nr", newApiOrder.orderId.toString())
            val checkApiOrder = DelawareApi.retrofitService.putOrder(newApiOrderPost).await()
        } catch (e: Exception){

        }

        database.orderDatabaseDao.insert(newApiOrder.asDatabaseOrder())


        items.forEach { o ->
            val newOrderItemApi = ApiOrderItem(
                orderItemId = o.orderItemId,
                quantity = o.quantity,
                totalPrice = o.totalPrice,
                productId = o.productId,
                orderId = o.orderId
            )
            orderItemDatabase.orderItemDatabaseDao.insert(newOrderItemApi.asDatabaseModel())
        }

        return newOrder
    }

    suspend fun updateOrder(order: Order, packageApi: Package) {
        val newApiOrderPut = ApiOrderPut(
            orderId = order.orderId,
            netPrice = order.netPrice,
            orderDate = order.orderDate,
            statusDate = order.statusDate,
            orderStatus = order.orderStatus,
            deliveryAdress = order.deliveryAdress,
            invoiceAdress = order.deliveryAdress,
            userId = order.userId,
            taxAmount = order.taxAmount,
            totalAmount = order.totalAmount,
        )

        val newApiOrder = ApiOrder(
            orderId = order.orderId,
            netPrice = order.netPrice,
            orderDate = order.orderDate,
            statusDate = order.statusDate,
            orderStatus = order.orderStatus,
            deliveryAdress = order.deliveryAdress,
            invoiceAdress = order.deliveryAdress,
            userId = order.userId,
            packageId = order.packageId,
            taxAmount = order.taxAmount,
            totalAmount = order.totalAmount,
            packageApi = packageApi,
        )

        try {
            val checkApiOrder = DelawareApi.retrofitService.changeOrder(order.orderId, newApiOrderPut).await()
            database.orderDatabaseDao.update(newApiOrder.asDatabaseOrder())
        } catch (e: Exception) {

        }
    }
}