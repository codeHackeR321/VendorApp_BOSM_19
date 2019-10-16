package com.example.vendorapp.homeactivity.viewmodel;

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.shared.singletonobjects.model.OrderRepository
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class MainScreenViewModel(context: Context) : ViewModel() {
    var orderRepository: OrderRepository = OrderRepositoryInstance.getInstance(context)
    var error : LiveData<String> = MutableLiveData()
    var logoutState : LiveData<String> = MutableLiveData()
    fun logout(){

        orderRepository.logout().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            (logoutState as MutableLiveData<String>).postValue("")

        },{
            (error as MutableLiveData<String>).postValue(" and restart the app")

        })
    }
}
