package com.example.vendorapp.acceptedorderscreen.viewModel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AcceptedOrderViewModel(context : Context) : ViewModel(){

    var acceptedOrders : LiveData<List<ModifiedOrdersDataClass>> = MutableLiveData()
    var acceptedOrderRepo = OrderRepositoryInstance.getInstance(context)


    @SuppressLint("CheckResult")
    fun getAcceptedOrders() {

        var list = emptyList<ModifiedOrdersDataClass>()
        acceptedOrderRepo.getOrdersRoom().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doOnNext { acceptedOrderList ->
                acceptedOrderList.forEach { order ->
                                            var childList = emptyList<ChildDataClass>()
                        order.items.forEach {
                            childList.plus(ChildDataClass(itemId = it.itemId , itemName = "Default Name" , price = it.price , quantity = it.quantity))
                        }
                        list.plus(ModifiedOrdersDataClass(timestamp = order.order.timestamp.toString() , totalAmount = order.order.totalAmount , status = order.order.status , otp = order.order.otp , items = childList , orderId = order.order.orderId))
                    }
                    (acceptedOrders as MutableLiveData<List<ModifiedOrdersDataClass>>).postValue(list)
                }

    }


    fun changeStatus(orderId:String, status:String) {
        acceptedOrderRepo.updateStatus(orderId,status)
    }

    fun getPreviousOrders(){

        acceptedOrderRepo.updateOrders()
    }

}

