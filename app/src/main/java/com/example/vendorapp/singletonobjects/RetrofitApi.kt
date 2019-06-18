package com.example.vendorapp.singletonobjects

import com.example.vendorapp.dataclasses.retroClasses.EarningsPojo
import com.example.vendorapp.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.dataclasses.retroClasses.OrdersPojo
import io.reactivex.Flowable
import io.reactivex.Observable
import retrofit2.http.GET

interface RetrofitApi {

    @get:GET("/orders")
    val orders: Flowable<List<OrdersPojo>>

    @get:GET("/menu")
    val menu: Flowable<List<MenuPojo>>

    @get:GET("/earnings")
    val earnings: Observable<EarningsPojo>

    //Add post when api available
}