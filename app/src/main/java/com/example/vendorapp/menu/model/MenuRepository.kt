package com.example.vendorapp.menu.model

import android.app.Application
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
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

        return menuDao.getMenu()
    }

    val menuApi = menuApiCall.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object: SingleObserver<List<MenuPojo>>
        {

            override fun onSuccess(t: List<MenuPojo>) {

                var menu = emptyList<MenuItemData>()

                t.forEach {
                    menu.plus(it.toMenuItemData())
                }

                menuDao.deleteAll()
                menuDao.insertMenu(menu)
            }

            override fun onSubscribe(d: Disposable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(e: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })

    private fun MenuPojo.toMenuItemData(): MenuItemData{

        return MenuItemData(itemId, name, price, status)

    }
}