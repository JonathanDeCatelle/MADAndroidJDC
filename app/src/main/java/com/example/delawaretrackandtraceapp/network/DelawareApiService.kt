package com.example.delawaretrackandtraceapp.network

import com.example.delawaretrackandtraceapp.domain.Order
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.util.*
import com.example.delawaretrackandtraceapp.domain.Package
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL = "https://10.0.2.2:7148/api/"
private const val BASE_URL_REAL = "https://20.86.168.67:7148/api/"
private const val BASE_URL_HTTP = "http://10.0.2.2:5056/api/"

// create moshi object
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(DateJsonAdapter())
    .add(PackageJsonAdapter())
    .add(OrderJsonAdapter())
    .build()

val packageAdapter: JsonAdapter<Package> = moshi.adapter(Package::class.java)
val orderAdapter: JsonAdapter<Order> = moshi.adapter(Order::class.java)

private val logger = HttpLoggingInterceptor().apply{level = HttpLoggingInterceptor.Level.BASIC}

//private val client = OkHttpClient.Builder()
//    //.addInterceptor(logger)
//    .build()

private val fakeClient = createOkHttpClient()

//Scalars Converter = converter for strings to plain text bodies
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL_HTTP)
    .client(fakeClient)
    //.client(client)
    .build()

interface DelawareApiService{
    @GET("product")
    fun getProducts(): Deferred<ApiProductContainer>

    @GET("order")
    fun getOrders(): Deferred<ApiOrderContainer>

    @GET("order/{id}")
    fun getOrder(@Path("id") id: String): Deferred<OneApiOrderContainer>

    @GET("notification")
    fun getNotifications(): Deferred<ApiNotificationContainer>

    @DELETE("notification/{id}")
    fun deleteNotificationByIdAsync(@Path("id") id: String): Deferred<ApiNotificationContainer>

    @PUT("notification")
    fun putNotification(@Body notification: ApiNotification): Deferred<ApiNotification>

    @POST("order")
    fun putOrder(@Body order: ApiOrderPost): Deferred<OneApiOrderPostContainer>

    @GET("order/notifications/{id}")
    fun getNotificationsOfAnOrder(@Path("id") id: String): Deferred<ApiNotificationsOrderContainer>

    @PUT("order/{id}")
    fun changeOrder(@Path("id") id: String, @Body order: ApiOrderPut): Deferred<OneApiOrderPutContainer>
}

object DelawareApi {
    //lazy properties = thread safe --> can only be initialized once at a time
    //adds extra safety to our 1 instance we need.
    val retrofitService : DelawareApiService by lazy {
        retrofit.create(DelawareApiService::class.java)
    }

    // TODO: Nog in te vullen
}