package com.example.vendorapp.neworderscreen.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewOrderViewModel(context : Context) : ViewModel(){

    var orders : LiveData<List<ModifiedOrdersDataClass>> = MutableLiveData()
    var orderRepo = OrderRepositoryInstance.getInstance(context)
    var error : LiveData<String> = MutableLiveData()


    @SuppressLint("CheckResult")
    fun getNewOrders() {
        orderRepo.getAllNewOrders().observeOn(AndroidSchedulers.mainThread()).doOnNext {
            (orders as MutableLiveData<List<ModifiedOrdersDataClass>>).postValue(it)
        }.doOnError {

            Log.e("Testing NO VM" , "Error in reading new orders from database")
            (error as MutableLiveData<String>).postValue("Error in database. Please try after some time")
        }.subscribe()
    }

    fun refreshOrderData(){
        orderRepo.updateOrders().doOnComplete {
            getNewOrders()
        }.doOnError {
            (error as MutableLiveData<String>).postValue("Error in database. Please try after some time")
        }.subscribe()
    }
}