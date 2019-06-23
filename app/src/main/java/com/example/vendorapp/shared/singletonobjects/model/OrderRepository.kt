package com.example.vendorapp.shared.singletonobjects.model

import android.content.Context
import android.util.Log
import com.example.vendorapp.shared.singletonobjects.model.room.OrderDao
import com.example.vendorapp.shared.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.OrderItemsData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
import io.reactivex.*
import io.reactivex.schedulers.Schedulers

class OrderRepository(application: Context) {

    private val orderDao: OrderDao
    private val orderApiCall: Single<List<OrdersPojo>>

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        orderDao = database.orderDao()

        orderApiCall = RetrofitInstance.getRetroInstance().orders
    }

    fun getOrderFromId(orderId: String): Flowable<OrdersData> {
        return orderDao.getOrderById(orderId).subscribeOn(Schedulers.io())
    }

    fun updateStatus(orderId: String, status: String): Completable{
        return orderDao.updateStatus(orderId, status).subscribeOn(Schedulers.io())
    }


    fun getOrdersRoom(): Flowable<List<OrderItemsData>>{
        return orderDao.getOrders().subscribeOn(Schedulers.io())
            .flatMap {
                var orderList = emptyList<OrderItemsData>()
                it.forEach { ordersData ->
                    orderDao.getItemsForOrder(ordersData.orderId)
                        .doOnNext{itemList ->
                            orderList=orderList.plus(OrderItemsData(ordersData, itemList))

                        }.subscribe()
                }
                return@flatMap Flowable.just(orderList)
            }
    }

    fun getNewOrders(): Flowable<List<OrderItemsData>>{
        Log.d("check","called")
        return orderDao.getNewOrders().subscribeOn(Schedulers.io())
            .flatMap {
                var orderList = emptyList<OrderItemsData>()
                it.forEach { ordersData ->
                    orderDao.getItemsForOrder(ordersData.orderId)
                        .doOnNext{itemList ->
                            orderList=orderList.plus(OrderItemsData(ordersData, itemList))
                            Log.d("check1",orderList.toString())

                        }.subscribe()
                    Log.d("check2",orderList.toString())
                }
                Log.d("check2",orderList.toString())
                return@flatMap Flowable.just(orderList)
            }
    }


            fun updateOrders(): Completable {

        return orderApiCall.subscribeOn(Schedulers.io())
            .doOnSuccess {
                 Log.d("checkP","$it")
                var orders = emptyList<OrdersData>()
                var items = emptyList<ItemData>()

                it.forEach { ordersPojo ->

                    orders= orders.plus(ordersPojo.toOrderData())
                    items= items.plus(ordersPojo.toItemData())
                }
                Log.d("check",orders.toString())
                Log.d("check",items.toString())
                orderDao.deleteAllOrders()
                orderDao.deleteAllOrderItems()
                orderDao.insertOrders(orders)
                orderDao.insertOrderItems(items)
            }
            .ignoreElement()
    }

    private fun OrdersPojo.toOrderData(): OrdersData{
        return OrdersData(orderId = orderId, status = status, otp = otp, timestamp = timestamp.toLong(), totalAmount = totalAmount)
    }

    private fun OrdersPojo.toItemData(): List<ItemData>{

        var item = emptyList<ItemData>()

        items.forEach {
           item = item.plus(ItemData(itemId = it.itemId, price = it.price, quantity = it.quantity, orderId = orderId, id = 0))
        }
        return item
    }
}