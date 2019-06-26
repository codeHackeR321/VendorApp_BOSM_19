package com.example.vendorapp.shared.singletonobjects.model

import android.content.Context
import android.util.Log
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.completedorderscreen.model.room.EarningDao
import com.example.vendorapp.shared.singletonobjects.model.room.OrderDao
import com.example.vendorapp.shared.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.OrderItemsData
import com.example.vendorapp.shared.dataclasses.retroClasses.DayPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.EarningsPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.EarningData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
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
    fun getAcceptedOrdersRoom(): Flowable<List<ModifiedOrdersDataClass>>{
        return orderDao.getAllAcceptedOrders().subscribeOn(Schedulers.io())
            .flatMap {
                var list = it.sortedBy { it.orderId }
                var orderItemList = emptyList<ModifiedOrdersDataClass>()
                var itemList = emptyList<ChildDataClass>()
                for ((index , item) in list.iterator().withIndex())
                {
                    itemList = itemList.plus(ChildDataClass(itemId = item.itemId , price = item.price , quantity = item.quantity , itemName = item.name))
                    if (!(index != list.size - 1 && list[index].orderId == list[index + 1].orderId))
                    {
                        orderItemList = orderItemList.plus(ModifiedOrdersDataClass(orderId = item.orderId , status = item.status , timestamp = item.timestamp.toString() , otp = item.otp , totalAmount = item.totalAmount , items = itemList))
                        itemList = emptyList<ChildDataClass>()
                    }
                }
                return@flatMap Flowable.just(orderItemList)
            }.doOnError {
                Log.e("Testing Repo" , "Error in reading database = ${it.message.toString()}")
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


    fun getAllNewOrders() : Flowable<List<ModifiedOrdersDataClass>>
    {
        Log.d("Testing Repo" , "Entered to get orders")
        return orderDao.getAllNewOrders().subscribeOn(Schedulers.io())
            .flatMap {
                Log.d("Testing Repo" , "Accepted Orders Recived = ${it.toString()}")
                var list = it.sortedBy { it.orderId }
                var orderItemList = emptyList<ModifiedOrdersDataClass>()
                var itemList = emptyList<ChildDataClass>()
                for ((index , item) in list.iterator().withIndex())
                {
                    itemList = itemList.plus(ChildDataClass(itemId = item.itemId , price = item.price , quantity = item.quantity , itemName = item.name))
                    if (!(index != list.size - 1 && list[index].orderId == list[index + 1].orderId))
                    {
                        orderItemList = orderItemList.plus(ModifiedOrdersDataClass(orderId = item.orderId , status = item.status , timestamp = item.timestamp.toString() , otp = item.otp , totalAmount = item.totalAmount , items = itemList))
                        itemList = emptyList<ChildDataClass>()
                    }
                }
                Log.d("Testing Repo" , "Returned From Repo = ${orderItemList.toString()}")
                return@flatMap Flowable.just(orderItemList)
        }.doOnError {
            Log.e("Testing Repo" , "Error in reading database = ${it.message.toString()}")
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
                Log.e("Finish1","Error getting room data${it}")
            }
    }

    //update room with earnings data
    fun updateEarningsData(): Completable{

        return earningsApiCall.subscribeOn(Schedulers.io())
            .doOnSuccess {

                var daywiseEarnings = emptyList<EarningData>()

               it.daywise.forEach { dayPojo ->

                   daywiseEarnings=daywiseEarnings.plus(dayPojo.toEarningData())
               }
                Log.d("check",daywiseEarnings.toString())
                earningDao.insertEarningData(daywiseEarnings)
            }.doOnError {
                Log.e("Finish2", "error getting data from backend$it")
            }
            .ignoreElement()
    }

    private fun DayPojo.toEarningData(): EarningData{
        return EarningData(day = day,earnings = earnings)
    }

    // get earnings data From ROOM
    fun getdaywiseEarningRoom(): Flowable<List<EarningData>>{
        return earningDao.getDayWiseEarnings().subscribeOn(Schedulers.io()).doOnError {
            Log.e("Finish3", "error getting data from roomk daywise$it")
        }
    }

  /*  fun getOverallEarningsRoom():Single<Long>{
        return earningDao.getOverallEarnings().subscribeOn(Schedulers.io()).doOnError {
            Log.e("Finish4", "$it")
        }
    }*/

    fun updateOrders(): Completable {

        return orderApiCall.subscribeOn(Schedulers.io())
            .doOnSuccess {
                 Log.d("Testing Repo","Api success with ${it.toString()}")
                var orders = emptyList<OrdersData>()
                var items = emptyList<ItemData>()

                it.forEach { ordersPojo ->

                    orders = orders.plus(ordersPojo.toOrderData())
                    items = items.plus(ordersPojo.toItemData())

                }
                Log.d("Testing Repo" , "Orders Added = ${orders.toString()}")
                Log.d("Testing Repo" , "Items added = ${items.toString()}")
                orderDao.deleteAllOrderItems()
                orderDao.insertOrders(orders)
                orderDao.insertOrderItems(items)
            }.doOnError {
                Log.e("Testing Repo" , "Error in fetching data = ${it.message.toString()}")
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