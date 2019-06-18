package com.example.vendorapp.acceptedorderscreen.model

import android.app.Application
import com.example.vendorapp.acceptedorderscreen.model.room.AcceptedOrderDao
import com.example.vendorapp.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.singletonobjects.RetrofitInstance
import com.example.vendorapp.singletonobjects.VendorDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AcceptedOrderRepository(application: Application) {

    private val acceptedOrderDao: AcceptedOrderDao
    private val orderApiCall: Observable<List<OrdersPojo>>

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        acceptedOrderDao = database.acceptedOrderDao()

        orderApiCall = RetrofitInstance.getRetroInstance().orders
    }

    val ordersRoom = acceptedOrderDao.getOrders().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe{

        }

    val ordersApi = orderApiCall.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map {  }
        .subscribe()

}