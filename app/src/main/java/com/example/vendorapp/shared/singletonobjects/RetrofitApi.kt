package com.example.vendorapp.shared.singletonobjects

import com.example.vendorapp.shared.dataclasses.retroClasses.EarningsPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.OrdersPojo
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface RetrofitApi {

    @get:GET("orders")
    val orders: Single<List<OrdersPojo>>

    @get:GET("menu")
    val menu: Single<List<MenuPojo>>

    @get:GET("earnings")
    val earnings: Single<EarningsPojo>

    @POST("orders")
    fun updateStatus(@Body body: JsonObject):Single<Response<Unit>>
    //Add post when api available
}