package com.example.vendorapp.acceptedOrderScreen.model

import android.app.Application
import com.example.vendorapp.acceptedOrderScreen.model.room.AcceptedOrderDao
import com.example.vendorapp.dataClasses.retroClasses.OrdersPojo
import com.example.vendorapp.dataClasses.roomClasses.OrdersData
import com.example.vendorapp.singletonObjects.RetrofitInstance
import com.example.vendorapp.singletonObjects.VendorDatabase
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import retrofit2.Call

class AcceptedOrderRepository(application: Application) {

    private val acceptedOrderDao: AcceptedOrderDao
    private val orderApiCall: Call<List<OrdersPojo>>

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        acceptedOrderDao = database.acceptedOrderDao()

        orderApiCall = RetrofitInstance.getRetroInstance().orders
    }

    val orders = acceptedOrderDao.getOrders().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

}