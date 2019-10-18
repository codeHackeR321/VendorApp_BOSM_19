package com.example.vendorapp.shared.singletonobjects

import com.example.vendorapp.loginscreen.model.LoginResponse
import com.example.vendorapp.shared.dataclasses.retroClasses.DayPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.EarningsPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.OrdersPojo
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Single
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface RetrofitApi {

    @get:GET("orders")
    val orders: Single<List<OrdersPojo>>

  /*  @get:GET("menu")
    val menu: Single<List<MenuPojo>>*/


    @POST("wallet/vendor/items/toggle_availability")
    fun toogleItemAvailiblity(@Header("Authorization") jwt: String, @Body body: JsonObject) : Single<Response<Unit>>


    // Get the menu of the vendor
    @GET("wallet/vendor/{vendor_id}/items")
    fun getMenu(@Header("Authorization") jwt: String,@Path("vendor_id")vendor_id:String): Single<Response<List<MenuPojo>>>


    //get Earning Data
    @POST("wallet/vendor/earnings-list")
    fun getEarningData(@Header("Authorization") jwt: String,@Body body: JsonObject):Single<Response<List<DayPojo>>>

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

    @POST("wallet/vendor/orders/idlist")
    fun getOrdersFromOrderIds(@Header("Authorization") jwt: String,@Body body: JsonObject):Single<Response<List<OrdersPojo>>>
}