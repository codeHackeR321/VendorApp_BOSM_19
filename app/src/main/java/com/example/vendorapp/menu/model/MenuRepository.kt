package com.example.vendorapp.menu.model

import android.app.Application
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MenuRepository (application: Application){

    private val menuDao: MenuDao
    private val menuApiCall: Single<List<MenuPojo>>

    init {
        val database = VendorDatabase.getDatabaseInstance(application)
        menuDao = database.menuDao()

        menuApiCall = RetrofitInstance.getRetroInstance().menu
    }

    fun getMenuRoom(): Flowable<List<MenuItemData>>{
        return menuDao.getMenu().subscribeOn(Schedulers.io())
    }

    fun updateMenu(): Completable{

        return menuApiCall.subscribeOn(Schedulers.io())
            .doOnSuccess {

                var menu = emptyList<MenuItemData>()

                it.forEach { menuPojo ->
                    menu.plus(menuPojo.toMenuItemData())
                }

                menuDao.deleteAll()
                menuDao.insertMenu(menu)
            }
            .ignoreElement()
    }

    private fun MenuPojo.toMenuItemData(): MenuItemData{
        return MenuItemData(itemId, name, price, status)
    }

}