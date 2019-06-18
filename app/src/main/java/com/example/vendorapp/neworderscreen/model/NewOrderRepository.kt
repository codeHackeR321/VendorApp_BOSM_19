package com.example.vendorapp.neworderscreen.model

import android.app.Application
import com.example.vendorapp.neworderscreen.model.room.NewOrderDao
import com.example.vendorapp.singletonobjects.VendorDatabase

class NewOrderRepository(application: Application) {

    private val newOrderDao: NewOrderDao

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        newOrderDao = database.newOrderDao()
    }

}