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

  /*  @get:GET("menu")
    val menu: Single<List<MenuPojo>>*/

    @GET("wallet/vendor/{vendor_id}/items")
    fun getMenu(@Header("Authorization") jwt: String,@Path("vendor_id")vendor_id:String): Single<Response<List<MenuPojo>>>

    @get:GET("earnings")
    val earnings: Single<EarningsPojo>

    @POST("wallet/vendor/orders/{id}/change_status")
    fun updateStatus(@Header("Authorization") jwt: String,@Body body: JsonObject,@Path("id")order_id:String):Single<Response<Unit>>
    //Add post when api available

    //decline an order
    @POST("wallet/vendor/orders/{id}/decline")
    fun declineOrder(@Header("Authorization") jwt: String,@Path("id")order_id:Int):Single<Response<Unit>>

    //to obtain JWT
    @POST("wallet/auth")
    fun getJWTfromAuth(@Body body: JsonObject):Single<Response<LoginResponse>>

    //obtain order from orderid

     @GET("wallet/vendor/order/{id}")
     fun getOrderFromOrderId(@Header("Authorization") jwt: String,@Path("id")orderId:String):Single<Response<OrdersPojo>>
}