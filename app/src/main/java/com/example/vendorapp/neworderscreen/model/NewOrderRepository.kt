package com.example.vendorapp.neworderscreen.model

import android.annotation.SuppressLint
import android.app.Application
import com.example.vendorapp.dataclasses.retroClasses.ItemPojo
import com.example.vendorapp.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.dataclasses.roomClasses.ItemData
import com.example.vendorapp.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.neworderscreen.model.room.NewOrderDao
import com.example.vendorapp.singletonobjects.RetrofitInstance
import com.example.vendorapp.singletonobjects.VendorDatabase
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.internal.util.BackpressureHelper.add
import io.reactivex.schedulers.Schedulers
import java.util.*
import kotlin.collections.ArrayList

class NewOrderRepository(application: Application) {

    private val newOrderDao: NewOrderDao
    private val orderApi: Single<List<OrdersPojo>>

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        newOrderDao = database.newOrderDao()
        orderApi = RetrofitInstance.getRetroInstance().orders
    }

    @SuppressLint("CheckResult")
    fun getNewOrdersList():Flowable<List<OrdersPojo>>
    {
        return newOrderDao.getAllNewOrders()
    }

    fun setnewOrderfromServer()
    {
        orderApi.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map {
                it.forEach {
                    newOrderDao.insertNewOrder(
                        OrdersData(
                            it.orderId,
                            it.status,
                            it.timestamp,
                            it.otp,
                            it.totalAmount,
                            setItem(it.items)
                        )
                    )
                }  }
            .subscribe()
    }

    fun setItem(itemList: List<ItemPojo>): ArrayList<ItemData> {
        var item: ArrayList<ItemData> = ArrayList()
        for (i in 0 until itemList.size)
            item.add(ItemData(itemList.get(i).itemId, itemList.get(i).price, itemList.get(i).quantity))
        return item
    }

    fun updateStatus(orderId:String,status:String):Completable
    {
        return newOrderDao.updateStatus(orderId,status)
    }

}