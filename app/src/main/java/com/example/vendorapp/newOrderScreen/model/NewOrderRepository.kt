package com.example.vendorapp.newOrderScreen.model

import android.app.Application
import com.example.vendorapp.newOrderScreen.model.room.NewOrderDao
import com.example.vendorapp.singletonObjects.VendorDatabase

class NewOrderRepository(application: Application) {

    private val newOrderDao: NewOrderDao

    init {

        val database = VendorDatabase.getInstance(application)
        newOrderDao = database.newOrderDao()
    }

}