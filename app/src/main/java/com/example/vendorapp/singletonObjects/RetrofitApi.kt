package com.example.vendorapp.singletonObjects

import com.example.vendorapp.dataClasses.retroClasses.EarningsPojo
import com.example.vendorapp.dataClasses.retroClasses.MenuPojo
import com.example.vendorapp.dataClasses.retroClasses.OrdersPojo
import retrofit2.Call
import retrofit2.http.GET

interface RetrofitApi {

    @get:GET("/orders")
    val orders: Call<List<OrdersPojo>>

    @get:GET("/menu")
    val menu: Call<MenuPojo>

    @get:GET("/earnings")
    val earnings: Call<EarningsPojo>

    //Add post when api available
}