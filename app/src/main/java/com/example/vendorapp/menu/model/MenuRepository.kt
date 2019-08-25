package com.example.vendorapp.menu.model

import android.content.Context
import android.util.Log
import com.example.vendorapp.R
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.shared.singletonobjects.RetrofitApi
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
import com.example.vendorapp.shared.utils.NetworkConnectivityCheck
import com.google.gson.JsonObject
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class MenuRepository(val application: Context) {

    private val menuDao: MenuDao
    private val menuApiCall: RetrofitApi
    private val sharedPref = application.getSharedPreferences(
        application.getString(R.string.preference_file_login), Context.MODE_PRIVATE
    )
    private val defaultJWTValue = application.getString(R.string.default_jwt_value)
    val jwt_token = sharedPref.getString(application.getString(R.string.saved_jwt), defaultJWTValue)
   val  vendor_id = sharedPref.getString(application.getString(com.example.vendorapp.R.string.saved_vendor_id), application.getString(com.example.vendorapp.R.string.default_vendor_id))


    init {
        val database = VendorDatabase.getDatabaseInstance(application)
        menuDao = database.menuDao()

        menuApiCall = RetrofitInstance.getRetroInstance()
    }

    fun getMenuRoom(): Flowable<List<MenuItemData>> {
        return menuDao.getMenu().subscribeOn(Schedulers.io()).doOnError {
            Log.e("Error in Menu Repo", "Error in getting items from room = ${it.toString()}")
        }
    }


    fun updateMenu(): Completable {

        return menuApiCall.getMenu("JWT " + jwt_token!!, vendor_id = vendor_id).subscribeOn(Schedulers.io())
            .doOnSuccess {
                Log.d("Menu", "Menu api call code ${it.code()} ${it.message()}")
                var menu = emptyList<MenuItemData>()
                if (it.isSuccessful) {
                    it.body()!!.forEach { menuPojo ->
                        menu = menu.plus(menuPojo.toMenuItemData())
                    }
                }
                menuDao.insertMenu(menu)
            }.doOnError {
                Log.e("Testing Menu Repo", "Error in adding menu data to room = ${it.toString()}")
            }.ignoreElement()

    }

    fun updateItemStatus(id: Int, availability_state: Int ) : Single<Response<Unit>> {
        Log.d("MenuRepo", "Entered update status with jwt = $jwt_token\nid = $id")
        val body = JsonObject().also {
            it.addProperty("item_id", id)
            it.addProperty("new_availability_state",availability_state)

        }
        Log.d("Menu Repo", "Sent body = ${body.toString()}")
        return menuApiCall.toogleItemAvailiblity("JWT " + jwt_token!!, body).doOnError{  Log.d("crash","gg")}


    }

    private fun MenuPojo.toMenuItemData(): MenuItemData {
      /*  var is_avai_state=1
        if (is_available==false)
            is_avai_state=0
        else if (is_available==true)
            is_avai_state=1*/


        return MenuItemData(id, name, price, is_available)

    }

}