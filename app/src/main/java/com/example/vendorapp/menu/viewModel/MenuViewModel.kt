package com.example.vendorapp.menu.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.CheckResult
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.menu.model.MenuRepository
import com.example.vendorapp.menu.model.MenuStatus
import com.example.vendorapp.shared.UIState
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.shared.singletonobjects.repositories.MenuRepositoryInstance
import com.example.vendorapp.shared.utils.NetworkConnectivityCheck
import com.google.firebase.database.core.Repo
import io.reactivex.Flowable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MenuViewModel(context: Context) :ViewModel() {

    var menuList:LiveData<List<MenuItemData>> = MutableLiveData()
    var error : LiveData<String> = MutableLiveData()
    var menuRepository:MenuRepository=MenuRepositoryInstance.getInstance(context)
    var saveChangesSelectedListener: LiveData<Int> = MutableLiveData()
    var menuUIState: LiveData<UIState> = MutableLiveData()

    init {

        observeUIState()
        @SuppressLint("CheckResult")
        if (NetworkConnectivityCheck().checkIntenetConnection(context)) {
            menuRepository.updateMenu().subscribeOn(Schedulers.io()).subscribe({

                menuRepository.getMenuRoom().observeOn(AndroidSchedulers.mainThread())
                .subscribe({menu->
                    (menuList as MutableLiveData<List<MenuItemData>>).postValue(menu)
                },
                {
                    Log.d("Error",it.stackTrace.toString())
                })
            },
             {
                 Log.e("Error in Menu VM" , "Error in updating menu = ${it.toString()}")
                 (error as MutableLiveData<String>).postValue("Please check your internet connection and restart the app")
             })
        } else {
            (error as MutableLiveData<String>).postValue("Please check your internet connection and restart the app")
        }
    }

    @SuppressLint("CheckResult")
    fun updateStatus() {
        Log.d("MenuVM", "Entered update status")
        menuRepository.updateItemStatus()
    }

    @SuppressLint("CheckResult")
    fun getMenuFromRoom(){
        menuRepository.getMenuRoom().subscribeOn(Schedulers.io())
            .subscribe({menu->
                (menuList as MutableLiveData<List<MenuItemData>>).postValue(menu)

                Log.d("MenuViewModel1","menuList Room ${menuList}")
            },{
                Log.d("Error",it.stackTrace.toString())
            })

    }
    @SuppressLint("CheckResult")
    fun observeUIState() {
          menuRepository.getUIStateFlowable().subscribeOn(Schedulers.io()).subscribe({

              (menuUIState as MutableLiveData).postValue(it)
          },{
              (error as MutableLiveData<String>).postValue("Error in returning flwable menu activity")
          })
    }

    @SuppressLint("CheckResult")
    fun setSaveChangesSelectedListener(){

        menuRepository.getSaveChangesSelectedState().subscribeOn(Schedulers.io()).subscribe({

            (saveChangesSelectedListener as MutableLiveData).postValue(it)
        },{

            Log.d("Errror in room", "Not return save changes stauys$it")
        })
    }


    @SuppressLint("CheckResult")
    fun updateTempStatus(itemId : Int, newTempStatus: Int){

        menuRepository.setTempStatusRoom(itemId=itemId,newTempStatus = newTempStatus).subscribeOn(Schedulers.io()).subscribe({

            Log.d("Errror in room", " update temp stauys$")
        },{
            Log.d("Errror in room", "Not update temp stauys$it")
        })
    }
 }


