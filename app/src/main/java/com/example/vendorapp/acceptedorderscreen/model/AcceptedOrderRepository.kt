package com.example.vendorapp.acceptedorderscreen.model

import android.app.Application
import com.example.vendorapp.acceptedorderscreen.model.room.AcceptedOrderDao
import com.example.vendorapp.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.dataclasses.roomClasses.ItemData
import com.example.vendorapp.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.singletonobjects.RetrofitInstance
import com.example.vendorapp.singletonobjects.VendorDatabase
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

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

    fun getOrderItemsRoom(orderId: String): Flowable<List<ItemData>>{
        return acceptedOrderDao.getItemsForOrder(orderId)
    }

    val orderApi = orderApiCall.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<List<OrdersPojo>>{
                override fun onSuccess(t: List<OrdersPojo>) {

                    var orders = emptyList<OrdersData>()
                    var itemsInOrder = emptyList<ItemData>()

                    t.forEach {

                        itemsInOrder.plus(it.toItemData())
                        orders.plus(it.toOrderData())
                    }

                    acceptedOrderDao.deleteAllOrders()
                    acceptedOrderDao.deleteAllOrderItems()
                    acceptedOrderDao.insertOrder( orders )
                    acceptedOrderDao.insertOrderItems( itemsInOrder )

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                }

            })




    private fun OrdersPojo.toOrderData(): OrdersData{
        return OrdersData(orderId = orderId, status = status, otp = otp,
            timestamp = timestamp.toLong(), totalAmount = totalAmount)
    }

    private fun OrdersPojo.toItemData(): List<ItemData>{

        var item = emptyList<ItemData>()

        items.forEach {
            item.plus(ItemData(itemId = it.itemId, price = it.price, quantity = it.quantity, orderId = orderId, id = 0))
        }

        return item
    }
}