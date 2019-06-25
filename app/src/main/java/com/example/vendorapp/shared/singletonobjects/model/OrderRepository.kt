package com.example.vendorapp.shared.singletonobjects.model

import android.content.Context
import android.util.Log
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vendorapp.completedorderscreen.model.room.EarningDao
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.shared.dataclasses.ItemsModel
import com.example.vendorapp.shared.singletonobjects.model.room.OrderDao
import com.example.vendorapp.shared.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.OrderItemsData
import com.example.vendorapp.shared.dataclasses.retroClasses.DayPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.EarningsPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.EarningData
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
import io.reactivex.*
import io.reactivex.schedulers.Schedulers

class OrderRepository(application: Context) {

    private val orderDao: OrderDao
    private val orderApiCall: Single<List<OrdersPojo>>
    private val earningDao: EarningDao
    private val earningsApiCall: Single<EarningsPojo>

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        orderDao = database.orderDao()

        orderApiCall = RetrofitInstance.getRetroInstance().orders

        // for daywise earnings
        earningDao = database.earningDao()
        earningsApiCall = RetrofitInstance.getRetroInstance().earnings
    }

    fun getOrderFromId(orderId: String): Flowable<OrdersData> {
        return orderDao.getOrderById(orderId).subscribeOn(Schedulers.io())
    }

    fun updateStatus(orderId: String, status: String): Completable{
        return orderDao.updateStatus(orderId, status).subscribeOn(Schedulers.io())
    }


    // get accepted and ready orders from room
    fun getOrdersRoom(): Flowable<List<OrderItemsData>>{
        return orderDao.getOrders().subscribeOn(Schedulers.io())
            .flatMap {
                var orderList = emptyList<OrderItemsData>()
                it.forEach { ordersData ->
                    orderDao.getItemsForOrder(ordersData.orderId)
                        .doOnSuccess{itemList ->
                            orderList = orderList.plus(OrderItemsData(ordersData, itemList))
                        }.subscribe()

                }
                Log.d("CheckAcceptedList",orderList.size.toString())
                return@flatMap Flowable.just(orderList)
            }
    }

    fun getNewOrders(): Flowable<List<OrderItemsData>>{
        Log.d("check","called")
        return orderDao.getNewOrders().subscribeOn(Schedulers.io())
            .flatMap {
                var orderList = emptyList<OrderItemsData>()
                for (ordersData in it) {
                    orderDao.getItemsForOrder(ordersData.orderId)
                        .doOnSuccess{itemList ->
                            orderList=orderList.plus(OrderItemsData(ordersData, itemList))
                            Log.d("check1",orderList.toString())
                        }.subscribe()
                }
                Log.d("check2",orderList.toString())
                return@flatMap Flowable.just(orderList)
            }

    }


    fun getAllNewOrders() : Flowable<List<OrderItemsData>>
    {
        return orderDao.trialQuery().subscribeOn(Schedulers.io())
            .flatMap {
            var list = it.sortedBy { it.orderId }
            var orderItemList = emptyList<OrderItemsData>()
            var itemList = emptyList<ItemsModel>()
            for ((index , item) in list.iterator().withIndex())
            {
                itemList = itemList.plus(ItemsModel(item.itemId , item.price , item.quantity , item.name))
                if (!(index != list.size - 1 && list[index].orderId == list[index + 1].orderId))
                {
                    orderItemList = orderItemList.plus(OrderItemsData(OrdersData(item.orderId , item.status , item.timestamp , item.otp , item.totalAmount) , itemList))
                    itemList = emptyList<ItemsModel>()
                }
            }
            return@flatMap Flowable.just(orderItemList)
        }
    }


    // returns orders with status "finish"
    fun getFinishedOrdersFromRoom(): Flowable<List<OrderItemsData>>{
        return orderDao.getFinishOrders().subscribeOn(Schedulers.io())
            .flatMap {
                var orderList = emptyList<OrderItemsData>()
                it.forEach { ordersData ->
                    orderDao.getItemsForOrder(ordersData.orderId)
                        .doOnSuccess{itemList ->
                            orderList=orderList.plus(OrderItemsData(ordersData, itemList))
                        }.subscribe()
                }
                Log.d("CheckAcceptedList",orderList.size.toString())
                return@flatMap Flowable.just(orderList)
            }.doOnError {
                Log.e("Finsh1","Error getting room data${it}")
            }
    }

    //update room with earnings data
    fun updateEarningsData(): Completable{

        return earningsApiCall.subscribeOn(Schedulers.io())
            .doOnSuccess {

                var daywiseEarnings = emptyList<EarningData>()

               it.daywise.forEach { dayPojo: DayPojo ->

                   daywiseEarnings=daywiseEarnings.plus(dayPojo.toEarningData())
               }

                earningDao.deleteAll()
                earningDao.insertEarningData(daywiseEarnings)
            }.doOnError {
                Log.e("Finish2", "error getting data from backend$it")
            }
            .ignoreElement()
    }

    private fun DayPojo.toEarningData(): EarningData{
        return EarningData(day, earnings.toLong())
    }

    // get earnings data From ROOM
    fun getdaywiseEarningRoom(): Flowable<List<EarningData>>{
        return earningDao.getDayWiseEarnings().subscribeOn(Schedulers.io()).doOnError {
            Log.e("Finish3", "error getting data from roomk daywise$it")
        }
    }

    fun getOverallEarningsRoom():Flowable<Long>{
        return earningDao.getOverallEarnings().subscribeOn(Schedulers.io()).doOnError {
            Log.e("Finish4", "error getting data from roomk overall$it")
        }
    }

    fun updateOrders(): Completable {

        return orderApiCall.subscribeOn(Schedulers.io())
            .doOnSuccess {
                 Log.d("checkP","$it")
                var orders = emptyList<OrdersData>()
                var items = emptyList<ItemData>()

                it.forEach { ordersPojo ->

                    orders = orders.plus(ordersPojo.toOrderData())
                    items = items.plus(ordersPojo.toItemData())
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