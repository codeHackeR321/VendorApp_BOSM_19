package com.example.vendorapp.menu.model

import android.app.Application
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.singletonObjects.VendorDatabase

class MenuRepository (application: Application){

    private val menuDao: MenuDao

    init {

        val database = VendorDatabase.getInstance(application)
        menuDao = database.menuDao()
    }
}