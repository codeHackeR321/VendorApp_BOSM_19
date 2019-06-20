package com.example.vendorapp.shared.singletonobjects

import com.example.vendorapp.shared.dataclasses.retroClasses.EarningsPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.OrdersPojo
import io.reactivex.Single
import retrofit2.http.GET

interface RetrofitApi {

    @get:GET("/orders")
    val orders: Single<List<OrdersPojo>>

    @get:GET("/menu")
    val menu: Single<List<MenuPojo>>

    @get:GET("/earnings")
    val earnings: Single<EarningsPojo>

    //Add post when api available
}