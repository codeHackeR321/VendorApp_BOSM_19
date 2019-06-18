package com.example.vendorapp.acceptedOrderScreen.model

import android.app.Application
import com.example.vendorapp.acceptedOrderScreen.model.room.AcceptedOrderDao
import com.example.vendorapp.dataClasses.retroClasses.OrdersPojo
import com.example.vendorapp.dataClasses.roomClasses.OrdersData
import com.example.vendorapp.singletonObjects.RetrofitInstance
import com.example.vendorapp.singletonObjects.VendorDatabase
import io.reactivex.CompletableObserver
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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