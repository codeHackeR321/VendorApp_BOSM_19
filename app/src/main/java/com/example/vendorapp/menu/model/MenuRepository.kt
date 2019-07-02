package com.example.vendorapp.menu.model

import android.content.Context
import android.util.Log
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
import com.example.vendorapp.shared.utils.NetworkConnectivityCheck
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MenuRepository(val application: Context){

    private val menuDao: MenuDao
    private val menuApiCall: Single<List<MenuPojo>>

    init {
        val database = VendorDatabase.getDatabaseInstance(application)
        menuDao = database.menuDao()

        menuApiCall = RetrofitInstance.getRetroInstance().menu
    }

    fun getMenuRoom(): Flowable<List<MenuItemData>>{
        return menuDao.getMenu().subscribeOn(Schedulers.io()).doOnError {
            Log.e("Error in Menu Repo" , "Error in getting items from room = ${it.toString()}")
        }
    }

    fun updateMenu(): Completable{

    return menuApiCall.subscribeOn(Schedulers.io())
            .doOnSuccess {

                var menu = emptyList<MenuItemData>()

                it.forEach { menuPojo ->
                   menu= menu.plus(menuPojo.toMenuItemData())
                }

                menuDao.insertMenu(menu)
            }.doOnError {
                Log.e("Testing Menu Repo" , "Error in adding menu data to room = ${it.toString()}")
            }
            .ignoreElement()

    }

    private fun MenuPojo.toMenuItemData(): MenuItemData{
        return MenuItemData(itemId, name, price, status)
    }

}