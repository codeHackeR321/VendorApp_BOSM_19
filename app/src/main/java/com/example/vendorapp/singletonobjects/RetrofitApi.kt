package com.example.vendorapp.singletonobjects

import com.example.vendorapp.dataclasses.retroClasses.EarningsPojo
import com.example.vendorapp.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.dataclasses.retroClasses.OrdersPojo
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