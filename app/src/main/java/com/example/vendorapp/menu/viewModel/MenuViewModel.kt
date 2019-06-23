package com.example.vendorapp.menu.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.menu.model.MenuRepository
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.shared.singletonobjects.repositories.MenuRepositoryInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MenuViewModel(context: Context) :ViewModel() {

    var menuList:LiveData<List<MenuItemData>> = MutableLiveData()
    var menuRepository:MenuRepository=MenuRepositoryInstance.getInstance(context)
    init {

        menuRepository.updateMenu().subscribeOn(Schedulers.io()).subscribe(
            {
                menuRepository.getMenuRoom().observeOn(AndroidSchedulers.mainThread())
                    .subscribe({menu->
                        (menuList as MutableLiveData<List<MenuItemData>>).postValue(menu)
                    },{
                        Log.d("Error",it.stackTrace.toString())
                    })
            }
        )
    }

    fun updateStatus(id:String,status:String){

    }
}