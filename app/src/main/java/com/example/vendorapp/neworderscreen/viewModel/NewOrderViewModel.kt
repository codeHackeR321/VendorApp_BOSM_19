package com.example.vendorapp.neworderscreen.viewModel

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.neworderscreen.view.expandableRecyclerView.ChildDataClass
import com.example.vendorapp.singletonobjects.repositories.NewOrderRepositoryInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewOrderViewModel(application: Application) : ViewModel(){

    var orders : LiveData<List<ModifiedOrdersDataClass>> = MutableLiveData()
    var newOrderRepo = NewOrderRepositoryInstance.getInstance(application)

    @SuppressLint("CheckResult")
    fun getNewOrders()
    {
        var list = emptyList<ModifiedOrdersDataClass>()
        newOrderRepo.getNewOrdersList().subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                doOnNext {orderList ->
                    orderList.forEach {order ->
                        var childList = emptyList<ChildDataClass>()
                        order.items.forEach {
                            childList.plus(ChildDataClass(itemId = it.itemId , itemName = "Default Name" , price = it.price , quantity = it.quantity))
                        }
                        list.plus(ModifiedOrdersDataClass(timestamp = order.timestamp , totalAmount = order.totalAmount , status = order.status , otp = order.otp , items = childList , orderId = order.orderId))
                    }
                    (orders as MutableLiveData<List<ModifiedOrdersDataClass>>).postValue(list)
                }
    }

    fun refreshOrderData()
    {
        newOrderRepo.setnewOrderfromServer()
    }

}