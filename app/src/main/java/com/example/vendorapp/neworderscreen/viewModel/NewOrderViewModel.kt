package com.example.vendorapp.neworderscreen.viewModel

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import com.example.vendorapp.shared.singletonobjects.repositories.NewOrderRepositoryInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewOrderViewModel(context : Context) : ViewModel(){

    var orders : LiveData<List<ModifiedOrdersDataClass>> = MutableLiveData()
    var newOrderRepo = NewOrderRepositoryInstance.getInstance(context)


    @SuppressLint("CheckResult")
    fun getNewOrders() {

        var ordersList = emptyList<ModifiedOrdersDataClass>()
        newOrderRepo.getNewOrdersList().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
            .doOnNext {orderList ->
                orderList.forEach {order ->
                    var itemsList = emptyList<ChildDataClass>()
                    newOrderRepo.getItemsOfOrder(order.orderId).observeOn(AndroidSchedulers.mainThread()).
                        doOnNext {
                            it.forEach {
                                itemsList = itemsList.plus(ChildDataClass("Item Name" , it.price , it.quantity , it.itemId))
                            }
                        }.
                        doOnComplete {
                            ordersList = ordersList.plus(ModifiedOrdersDataClass(order.orderId , order.status , order.timestamp.toString() , order.otp , order.totalAmount , itemsList))
                    }
                }
                (orders as MutableLiveData<List<ModifiedOrdersDataClass>>).postValue(ordersList)
            }
    }

    fun refreshOrderData() {
        newOrderRepo.setnewOrderfromServer()
    }
}