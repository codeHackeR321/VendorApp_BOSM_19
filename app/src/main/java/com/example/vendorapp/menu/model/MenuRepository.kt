package com.example.vendorapp.menu.model

import android.content.Context
import android.util.Log
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.shared.singletonobjects.RetrofitApi
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
import com.example.vendorapp.shared.utils.NetworkConnectivityCheck
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class MenuRepository(val application: Context){

    private val menuDao: MenuDao
   private val menuApiCall: RetrofitApi

    init {
        val database = VendorDatabase.getDatabaseInstance(application)
        menuDao = database.menuDao()

        menuApiCall = RetrofitInstance.getRetroInstance()
    }

    fun getMenuRoom(): Flowable<List<MenuItemData>>{
        return menuDao.getMenu().subscribeOn(Schedulers.io()).doOnError {
            Log.e("Error in Menu Repo" , "Error in getting items from room = ${it.toString()}")
        }
    }

    fun updateMenu(): Completable{

    return menuApiCall.getMenu("Basic VmVuZG9yMTpmaXJlYmFzZXN1Y2tz","1").subscribeOn(Schedulers.io())
            .doOnSuccess {
   Log.d("Menu","Menu api call code ${it.code()}")
                var menu = emptyList<MenuItemData>()
if (it.isSuccessful) {
    it.body()!!.forEach{ menuPojo ->
        menu = menu.plus(menuPojo.toMenuItemData())
    }
}
                menuDao.insertMenu(menu)
            }.doOnError {
                Log.e("Testing Menu Repo" , "Error in adding menu data to room = ${it.toString()}")
            }
            .ignoreElement()

    }

    private fun MenuPojo.toMenuItemData(): MenuItemData{
        return MenuItemData(id, name, price,  is_available)

    }

}