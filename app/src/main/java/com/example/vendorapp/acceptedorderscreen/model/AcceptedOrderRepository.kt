package com.example.vendorapp.acceptedorderscreen.model

import android.app.Application
import android.content.Context
import com.example.vendorapp.acceptedorderscreen.model.room.AcceptedOrderDao
import com.example.vendorapp.shared.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.OrderItemsData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.reactivestreams.Subscription

class AcceptedOrderRepository(application: Context) {

    private val acceptedOrderDao: AcceptedOrderDao
    private val orderApiCall: Single<List<OrdersPojo>>

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        acceptedOrderDao = database.acceptedOrderDao()

        orderApiCall = RetrofitInstance.getRetroInstance().orders
    }

//    fun getOrdersRoom(): Flowable<List<OrdersData>>{
//        return acceptedOrderDao.getOrders().subscribeOn(Schedulers.io())
//    }

//    fun getOrderItemsRoom(orderId: String): Flowable<List<ItemData>>{
//        return acceptedOrderDao.getItemsForOrder(orderId).subscribeOn(Schedulers.io())
//    }

    fun updateStatus(orderId: String, status: String): Completable{
        return acceptedOrderDao.updateStatus(orderId, status).subscribeOn(Schedulers.io())
    }


    fun getOrdersRoom(): Flowable<List<OrderItemsData>>{

        return acceptedOrderDao.getOrders().subscribeOn(Schedulers.io())
            .flatMap {
                var orderList = emptyList<OrderItemsData>()

                it.forEach { ordersData ->

                    acceptedOrderDao.getItemsForOrder(ordersData.orderId)
                        .doOnNext {itemList ->

                            orderList.plus(OrderItemsData(ordersData, itemList))
                        }
                }

                return@flatMap Flowable.just(orderList)
            }
    }

    fun updateOrders(): Completable {

        return orderApiCall.subscribeOn(Schedulers.io())
            .doOnSuccess {

                var orders = emptyList<OrdersData>()
                var items = emptyList<ItemData>()

                it.forEach { ordersPojo ->

                    orders.plus(ordersPojo.toOrderData())
                    items.plus(ordersPojo.toItemData())
                }

                acceptedOrderDao.deleteAllOrders()
                acceptedOrderDao.deleteAllOrderItems()
                acceptedOrderDao.insertOrder(orders)
                acceptedOrderDao.insertOrderItems(items)
            }
            .ignoreElement()
    }

    private fun OrdersPojo.toOrderData(): OrdersData{
        return OrdersData(orderId = orderId, status = status, otp = otp, timestamp = timestamp.toLong(), totalAmount = totalAmount)
    }

    private fun OrdersPojo.toItemData(): List<ItemData>{

        var item = emptyList<ItemData>()

        items.forEach {
            item.plus(ItemData(itemId = it.itemId, price = it.price, quantity = it.quantity, orderId = orderId, id = 0))
        }

        return item
    }
}