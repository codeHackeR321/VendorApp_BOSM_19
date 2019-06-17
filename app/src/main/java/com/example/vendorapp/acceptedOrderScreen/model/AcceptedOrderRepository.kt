package com.example.vendorapp.acceptedOrderScreen.model

import android.app.Application
import com.example.vendorapp.acceptedOrderScreen.model.room.AcceptedOrderDao
import com.example.vendorapp.singletonObjects.VendorDatabase

class AcceptedOrderRepository(application: Application) {

    private val acceptedOrderDao: AcceptedOrderDao

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        acceptedOrderDao = database.acceptedOrderDao()
    }

}