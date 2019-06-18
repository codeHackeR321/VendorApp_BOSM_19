package com.example.vendorapp.singletonObjects

import com.example.vendorapp.dataClasses.retroClasses.EarningsPojo
import com.example.vendorapp.dataClasses.retroClasses.MenuPojo
import com.example.vendorapp.dataClasses.retroClasses.OrdersPojo
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitApi {

    @get:GET("/orders")
    val orders: Observable<List<OrdersPojo>>

    @get:GET("/menu")
    val menu: Observable<List<MenuPojo>>

    @get:GET("/earnings")
    val earnings: Call<EarningsPojo>

    //Add post when api available
}