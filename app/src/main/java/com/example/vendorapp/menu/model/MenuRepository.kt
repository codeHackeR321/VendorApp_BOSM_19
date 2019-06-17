package com.example.vendorapp.menu.model

import android.app.Application
import com.example.vendorapp.dataClasses.retroClasses.MenuPojo
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.singletonObjects.RetrofitInstance
import com.example.vendorapp.singletonObjects.VendorDatabase
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuRepository (application: Application){

    private val menuDao: MenuDao
    private val menuApiCall: Observable<List<MenuPojo>>

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        menuDao = database.menuDao()

        menuApiCall = RetrofitInstance.getRetroInstance().menu
    }

    val menuListRoom = menuDao.getMenu().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe{

        }

    val menuApi = menuApiCall.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .map { }
        .subscribe()
}