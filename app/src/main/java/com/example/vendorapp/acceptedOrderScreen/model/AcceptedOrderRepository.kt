package com.example.vendorapp.acceptedOrderScreen.model

import android.app.Application
import com.example.vendorapp.acceptedOrderScreen.model.room.AcceptedOrderDao
import com.example.vendorapp.dataClasses.retroClasses.OrdersPojo
import com.example.vendorapp.singletonObjects.RetrofitInstance
import com.example.vendorapp.singletonObjects.VendorDatabase
import retrofit2.Call

class AcceptedOrderRepository(application: Application) {

    private val acceptedOrderDao: AcceptedOrderDao
    private val orderApiCall: Call<List<OrdersPojo>>

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        acceptedOrderDao = database.acceptedOrderDao()

        orderApiCall = RetrofitInstance.getRetroInstance().orders
    }

}