package com.example.vendorapp.menu.model

import android.app.Application
import com.example.vendorapp.dataClasses.retroClasses.MenuPojo
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.singletonObjects.RetrofitInstance
import com.example.vendorapp.singletonObjects.VendorDatabase
import retrofit2.Call

class MenuRepository (application: Application){

    private val menuDao: MenuDao
    private val menuApiCall: Call<MenuPojo>

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        menuDao = database.menuDao()

        menuApiCall = RetrofitInstance.getRetroInstance().menu
    }
}