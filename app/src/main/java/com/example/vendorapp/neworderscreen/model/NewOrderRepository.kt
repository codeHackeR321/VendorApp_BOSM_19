package com.example.vendorapp.neworderscreen.model

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.util.Log
import com.example.vendorapp.shared.dataclasses.retroClasses.ItemPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.neworderscreen.model.room.NewOrderDao
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

class NewOrderRepository(context: Context) {

    private val newOrderDao: NewOrderDao
    private val orderApi: Single<List<OrdersPojo>>

    init {

        val database = VendorDatabase.getDatabaseInstance(context)
        newOrderDao = database.newOrderDao()
        orderApi = RetrofitInstance.getRetroInstance().orders
    }

    @SuppressLint("CheckResult")
    fun getNewOrdersList():Flowable<List<OrdersData>>
    {
        Log.d("Testing Repo" , "Entered getNewOrderList")
        return newOrderDao.getAllNewOrders().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun setnewOrderfromServer()
    {
        Log.d("Testing Repo" , "Entered function to fetch from server")
        orderApi.subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map {orders ->
                orders.forEach {order ->
                    Log.d("Testing Repo" , "Server Data = ${order.toString()}")
                    newOrderDao.insertNewOrder(
                        OrdersData(
                            order.orderId,
                            order.status,
                            order.timestamp.toLong(),
                            order.otp,
                            order.totalAmount
                            )
                        )
                    var list = emptyList<ItemData>()
                    order.items.map {item ->
                        list.plus(ItemData(itemId = item.itemId , orderId = order.orderId , quantity = item.quantity , price = item.price , id = 0))
                    }
                    newOrderDao.insertItems(list)
                }
            }
            .subscribe()
    }

    fun setItem(itemList: List<ItemPojo>): ArrayList<ItemData> {
        var item: ArrayList<ItemData> = ArrayList()

        //Change orderId extraction process
        itemList.forEach {
            item.add(ItemData(id= 0, itemId = it.itemId, orderId = "0", price = it.price, quantity = it.quantity))
        }

        return item
    }

    fun updateStatus(orderId:String,status:String):Completable
    {
        return newOrderDao.updateStatus(orderId,status)
    }

    fun getItemName(id : String) = newOrderDao.getItemName(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())

}