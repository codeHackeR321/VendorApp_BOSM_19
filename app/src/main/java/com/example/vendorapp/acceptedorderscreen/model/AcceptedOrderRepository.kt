package com.example.vendorapp.acceptedorderscreen.model

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vendorapp.acceptedorderscreen.model.room.AcceptedOrderDao
import com.example.vendorapp.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.dataclasses.roomClasses.ItemData
import com.example.vendorapp.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.singletonobjects.RetrofitInstance
import com.example.vendorapp.singletonobjects.VendorDatabase
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.Observable

class AcceptedOrderRepository(application: Application) {

    private val acceptedOrderDao: AcceptedOrderDao
    private val orderApiCall: Single<List<OrdersPojo>>

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        acceptedOrderDao = database.acceptedOrderDao()

        orderApiCall = RetrofitInstance.getRetroInstance().orders
    }

    fun getOrdersRoom(): Flowable<List<OrdersData>>{

        return acceptedOrderDao.getOrders()
    }

    val orderApi = orderApiCall.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<OrdersPojo>>{
                override fun onSuccess(t: List<OrdersPojo>) {

                    var orders: ArrayList<OrdersData> = ArrayList(t.size)

                    t.forEach {

                        orders.add(it.toOrderData())
                    }

                    acceptedOrderDao.deleteAll()
                    acceptedOrderDao.insertOrder( orders )

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

            })




    private fun OrdersPojo.toOrderData(): OrdersData{

        var item: ArrayList<ItemData> = ArrayList(items.size)
        items.forEach {
            item.add(ItemData(itemId = it.itemId, price = it.price, quantity = it.quantity))
        }

        return OrdersData(orderId = orderId, status = status, otp = otp,
            timestamp = timestamp, totalAmount = totalAmount, items = item)
    }
}