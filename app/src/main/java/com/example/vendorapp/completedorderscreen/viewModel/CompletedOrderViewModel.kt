package com.example.vendorapp.completedOrderScreen.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.shared.singletonobjects.model.OrderRepository
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance

class CompletedOrderViewModel(context:Context) :ViewModel() {

    var earnings:LiveData<String> = MutableLiveData()
    var orderRepository:OrderRepository= OrderRepositoryInstance.getInstance(context)

    init{
        //orderRepository.getTotalEarnings().subscribe(earnings ->
        // {
        //(earnings as LiveData<String>).postValue(earnings)
        // },{
         //  Log.e("check",it.message.toString())
        // })
    }

    fun getOrdersForDate(date:String){

    }
}