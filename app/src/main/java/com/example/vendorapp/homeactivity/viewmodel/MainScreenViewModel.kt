package com.example.vendorapp.homeactivity.viewmodel;

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.shared.UIState
import com.example.vendorapp.shared.singletonobjects.model.OrderRepository
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainScreenViewModel(context: Context) : ViewModel() {
    var orderRepository: OrderRepository = OrderRepositoryInstance.getInstance(context)
    var error : LiveData<String> = MutableLiveData()
    var logoutState : LiveData<UIState> = MutableLiveData()
    @SuppressLint("CheckResult")
    fun logout(){

        orderRepository.logout().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).doOnComplete{
            Log.d("logout","oncomplte")
        }.subscribe({
            Log.d("logout","Sucess")
            (logoutState as MutableLiveData).postValue(UIState.LogoutSuccess)

        },{
            Log.d("logout","Fail")
            (logoutState as MutableLiveData).postValue(UIState.LogoutFail("Logout Fail try again $it"))

        })
    }
}
