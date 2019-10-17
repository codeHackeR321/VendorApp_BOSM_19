package com.example.vendorapp.menu.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.example.vendorapp.R
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.shared.UIState
import com.example.vendorapp.shared.singletonobjects.RetrofitApi
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
import com.example.vendorapp.shared.utils.NetworkConnectivityCheck
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject


class MenuRepository(val application: Context) {

    private val menuDao: MenuDao
    private val menuApiCall: RetrofitApi
    var ui_status_subject = BehaviorSubject.create<UIState>()
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

    @SuppressLint("CheckResult")
   /* fun updateItemStatus() {*/
    fun updateItemStatus(newStatusList: MutableList<MenuItemData>) {

       /* menuDao.getMenuListToBeUpdated().subscribeOn(Schedulers.io()).subscribe({newStatusList->

        },{
            Log.d("Error in room","Retriveing get menu list to nbe upadted")
        })
*/
        Log.d("MenuRepo", "Entered update status with jwt = $jwt_token\nlist = $newStatusList")
        if (newStatusList.isNotEmpty()){
            val body =getBody(newStatusList)
            Log.d("MenuRepo", "Sent body = ${body.toString()}")
            menuApiCall.toogleItemAvailiblity("JWT " + jwt_token!!, body).subscribeOn(Schedulers.io()).subscribe({
                Log.d("MenuRepo_API1", "update status of menu  code:${it.code()},body : $body,\n it.body:  ${it.body()} ")

                when (it.code()) {
                    200 -> {
                        //changeLoadingStatusRoom(orderId,isLoading = false)
                        Log.d("MenuRepo_API2","code : ${it.code()} new s ${body} it.body: ${it.body()} $")
                        menuDao.insertMenu(newStatusList)
                        ui_status_subject.onNext(UIState.SuccessStateChangeStatus("${it.code()}: body:${body} ${it.message()}"))
                        //display toast message
                    }
                    400 -> {
                        // show error message
                        //var json =JSONObject(it.errorBody().toString())

                        ui_status_subject.onNext(UIState.ErrorStateChangeStatus("${it.code()}:Something went wrong"))
                    }

                    else->{
                        ui_status_subject.onNext(UIState.ErrorStateChangeStatus(" Error ${it.code()}: ${it.message()} "))

                    }
                }
            }, {
                Log.d("MenuRepo_API15", "error apicall ,body:${body}, ERROR: $it")
                ui_status_subject.onNext(UIState.ErrorStateChangeStatus("EXCEPTION:  $it  body:${body}"))
            })

        }



    }

    private fun MenuPojo.toMenuItemData(): MenuItemData {
        var statusSave=false
        if (is_available==1)
            statusSave=true
         return MenuItemData(id, name, price, statusSave/*,-1*/)
    }
    fun getUIStateFlowable(): Flowable<UIState> {
        return ui_status_subject.toFlowable(BackpressureStrategy.LATEST).doOnError { Log.e("OrderRepo", "Failed to convert into uiState") }
    }

    private fun getBody(newStatusList: List<MenuItemData>):JsonObject{
       val body=JsonObject()
        val array=JsonArray()
         // api call me body status 0 ya 1 bhejna h true/ faolse nahi
        newStatusList.forEach {item->
            val element=JsonObject().also {
                var statusSent=0
                if (item.status)
                    statusSent=1

                it.addProperty("item_id", item.itemId)
                it.addProperty("new_availability_state", /*item.temp_status*//*item.status*/statusSent)
            }
            array.add(element)
        }

        body.add("item_obj_list",array)
        return body
    }

   /* fun getSaveChangesSelectedState():Flowable<Int>{
        return menuDao.getSaveChangesSelectedStatus()
    }

    fun setTempStatusRoom(itemId: Int,newTempStatus: Int): Completable{
        return menuDao.setNewTempStatus(itemId = itemId, tempStatus = newTempStatus)
    }*/

}