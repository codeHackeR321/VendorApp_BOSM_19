package com.example.vendorapp.shared.singletonobjects

import com.example.vendorapp.loginscreen.model.LoginResponse
import com.example.vendorapp.shared.dataclasses.retroClasses.EarningsPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.OrdersPojo
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {

    @get:GET("orders")
    val orders: Single<List<OrdersPojo>>

    @get:GET("menu")
    val menu: Single<List<MenuPojo>>

    @get:GET("earnings")
    val earnings: Single<EarningsPojo>

    @POST("orders/{id}/change_status")
    fun updateStatus(@Body body: JsonObject,@Path("id")orderId:String):Single<Response<Unit>>
    //Add post when api available

    //to obtain JWT
    @POST("wallet/auth")
    fun getJWTfromAuth(@Body body: JsonObject):Single<Response<LoginResponse>>

    //obtain order from orderid

     @GET("wallet/vendor/order/{order_id}")
     fun getOrderFromOrderId(@Header("Authorization") jwt: String,@Path("id")orderId:String):Single<Response<OrdersPojo>>
}