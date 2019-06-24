package com.example.vendorapp.acceptedorderscreen.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class AcceptedOrderViewModel(context : Context) : ViewModel(){


    var  acceptedOrders : LiveData<List<ModifiedOrdersDataClass>> = MutableLiveData()
    var orderRepo = OrderRepositoryInstance.getInstance(context)


    @SuppressLint("CheckResult")
    fun getAcceptedOrders(){

        var acceptedOrdersList = emptyList<ModifiedOrdersDataClass>()
        orderRepo.getOrdersRoom().observeOn(AndroidSchedulers.mainThread()).doOnNext {orderList ->
            orderList.forEach {order ->
                var itemList = emptyList<ChildDataClass>()
                for (item in order.items) {
                    itemList = itemList.plus(
                        ChildDataClass(
                            itemName = item.name,
                            itemId = item.itemId,
                            price = item.price,
                            quantity = item.quantity
                        ))
                }
                acceptedOrdersList = acceptedOrdersList.plus(ModifiedOrdersDataClass(
                    orderId = order.order.orderId,
                    otp = order.order.otp,
                    status = order.order.status,
                    totalAmount = order.order.totalAmount,
                    timestamp = order.order.timestamp.toString(),
                    items = itemList
                ))

                (acceptedOrders as MutableLiveData<List<ModifiedOrdersDataClass>>).postValue(acceptedOrdersList)
            }
        }.doOnError {
            Log.e("Testing NO VM" , it.localizedMessage)
        }.subscribe()
    }

    fun changeStatus(orderId:String, status:String) {
        orderRepo.updateStatus(orderId,status)
    }

    fun getPreviousOrders(){

        orderRepo.updateOrders().doOnComplete {
            getAcceptedOrders()
        }.subscribe()
    }
}

