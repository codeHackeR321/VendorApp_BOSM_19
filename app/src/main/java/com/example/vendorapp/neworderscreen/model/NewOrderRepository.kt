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
import com.example.vendorapp.shared.dataclasses.OrderItemsData
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
    fun getNewOrdersList(): Flowable<List<OrderItemsData>> {
        var orderList = emptyList<OrderItemsData>()
        newOrderDao.getAllNewOrders().subscribeOn(Schedulers.io()).doOnNext {orders->
            orders.forEach {order->
                newOrderDao.getOrderItems(order.orderId)
                    .doOnNext {
                        orderList=orderList.plus(OrderItemsData(order,it))
                    }
            }

        }
        return Flowable.just(orderList)
    }

    fun setnewOrderfromServer(): Completable {
        return orderApi.subscribeOn(Schedulers.io())
            .doOnSuccess { orders ->
                orders.forEach {
                    newOrderDao.insertNewOrder(it.toOrderData())
                    newOrderDao.insertOrderItems(it.toItemData())
                }
            }
            .ignoreElement()
    }

    fun getOrderFromId(orderId: String): Flowable<OrdersData> {
        return newOrderDao.getOrderById(orderId).subscribeOn(Schedulers.io())
    }

    fun updateStatus(orderId: String, status: String): Completable {
        return newOrderDao.updateStatus(orderId, status).subscribeOn(Schedulers.io())
    }

    fun OrdersPojo.toOrderData(): OrdersData {
        return OrdersData(orderId, status, timestamp.toLong(), otp, totalAmount)
    }

    fun OrdersPojo.toItemData(): List<ItemData> {

        var itemList = emptyList<ItemData>()
        items.forEach {
            itemList=itemList.plus(ItemData(0, it.itemId, orderId, it.price, it.quantity))
        }
        return itemList
    }

}