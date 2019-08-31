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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MenuViewModel(context: Context) :ViewModel() {

    var menuList:LiveData<List<MenuItemData>> = MutableLiveData()
    var error : LiveData<String> = MutableLiveData()
    var menuRepository:MenuRepository=MenuRepositoryInstance.getInstance(context)

    init {
        @SuppressLint("CheckResult")
        if (NetworkConnectivityCheck().checkIntenetConnection(context)) {
            menuRepository.updateMenu().subscribeOn(Schedulers.io()).subscribe({menuRepository.getMenuRoom().observeOn(AndroidSchedulers.mainThread())
                .subscribe({menu->
                    (menuList as MutableLiveData<List<MenuItemData>>).postValue(menu)
                },{
                    Log.d("Error",it.stackTrace.toString())
                })},{ Log.e("Error in Menu VM" , "Error in updating menu = ${it.toString()}")})
        } else {
            (error as MutableLiveData<String>).postValue("Please check your internet connection and restart the app")
        }
    }

    @SuppressLint("CheckResult")
    fun updateStatus(newStatusList: MutableList<MenuItemData>) {
        Log.d("MenuVM", "Entered update status")
        menuRepository.updateItemStatus(newStatusList)
    }

    @SuppressLint("CheckResult")
    fun observeUIState(): Flowable<UIState> {
        return  menuRepository.getUIStateFlowable().doOnError { Log.e("MenuViewModel", "UI State observer error hu") }
    }
}