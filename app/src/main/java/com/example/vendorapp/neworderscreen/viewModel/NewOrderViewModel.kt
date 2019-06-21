package com.example.vendorapp.neworderscreen.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import com.example.vendorapp.shared.singletonobjects.repositories.NewOrderRepositoryInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewOrderViewModel(context: Context) : ViewModel(){

    var orders : LiveData<List<ModifiedOrdersDataClass>> = MutableLiveData()
    var newOrderRepo = NewOrderRepositoryInstance.getInstance(context)

    @SuppressLint("CheckResult")
    fun getNewOrders()
    {
        Log.d("Testing model" , "Entered function to read orders")
        var list = emptyList<ModifiedOrdersDataClass>()
        newOrderRepo.getNewOrdersList().
                doOnNext {orderList ->
                    Log.d("Testing model" , "Data = ${orderList.toString()}")
                    orderList.forEach {order ->
                        var childList = emptyList<ChildDataClass>()
                        list = list.plus(ModifiedOrdersDataClass(timestamp = order.timestamp.toString() , totalAmount = order.totalAmount , status = order.status , otp = order.otp , items = childList , orderId = order.orderId))
                    }
                    Log.d("Testing ViewModel" , "Value of list = ${list.toString()}")
                    (orders as MutableLiveData<List<ModifiedOrdersDataClass>>).postValue(list)
                    Log.d("Testing ViewModel" , "Posted new data in orders. Orders = ${orders.value.toString()}")
                }.subscribe()

    }

    fun refreshOrderData()
    {
        Log.d("Testing model" , "Entered refresh function")
        newOrderRepo.setnewOrderfromServer()
    }

}