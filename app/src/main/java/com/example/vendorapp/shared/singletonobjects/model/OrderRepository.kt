package com.example.vendorapp.shared.singletonobjects.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log

import com.example.vendorapp.R
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.completedorderscreen.model.room.EarningDao
import com.example.vendorapp.loginscreen.view.UIState
import com.example.vendorapp.neworderscreen.model.IncompleteOrderStatus

import com.example.vendorapp.shared.singletonobjects.model.room.OrderDao
import com.example.vendorapp.shared.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.retroClasses.DayPojo
import com.example.vendorapp.shared.dataclasses.retroClasses.EarningsPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.EarningData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import com.example.vendorapp.shared.singletonobjects.RetrofitInstance
import com.example.vendorapp.shared.singletonobjects.VendorDatabase
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.json.JSONArray
import org.json.JSONObject

class OrderRepository(val application: Context) {

    private val orderDao: OrderDao
    private val orderApiCall: Single<List<OrdersPojo>>
    private val earningDao: EarningDao
    private val earningsApiCall: Single<EarningsPojo>
    private var jwt_token: String? = null
    private var vendor_id: String? = null
    private val orderService = RetrofitInstance.getRetroInstance()

    var ui_status_subject = BehaviorSubject.create<UIState>()
    var incomp_order_status_list = mutableListOf<IncompleteOrderStatus>()


    private val db = FirebaseFirestore.getInstance()


    private val sharedPref = application.getSharedPreferences(
        application.getString(R.string.preference_file_login), Context.MODE_PRIVATE
    )

    init {

        val database = VendorDatabase.getDatabaseInstance(application)
        orderDao = database.orderDao()

        orderApiCall = RetrofitInstance.getRetroInstance().orders

        // for daywise earnings
        earningDao = database.earningDao()
        earningsApiCall = RetrofitInstance.getRetroInstance().earnings
        jwt_token = sharedPref.getString(
            application.getString(R.string.saved_jwt),
            application.getString(R.string.default_jwt_value)
        )
        vendor_id = sharedPref.getString(
            application.getString(R.string.saved_vendor_id),
            application.getString(R.string.default_vendor_id)
        )
        Log.d("OrderRepo_API1", "Vendor id = $vendor_id")

        initRoom()
    }

    @SuppressLint("CheckResult")
    fun initRoom(){
orderDao.getAllOrdersRoom().subscribeOn(Schedulers.io()).subscribe({
    initFirestore(it)

},{
    Log.d("OrderRepoRoom","unable to get all order ids from room")
})


    }

    fun initFirestore(previousOrderIds: List<Int>) {
        //receive order id from firestore
        var order_id: String? = null
        db.collection("orders").whereEqualTo("vendorid", vendor_id!!.toInt()).addSnapshotListener { snapshots, e ->

            Log.d("OrderRepo_Firestore1", "Snapshot = ${snapshots.toString()}")
            Log.d("OrderRepo_Firestore2", "Error = ${e.toString()}")

            if (e != null) {
                Log.w("Sports", "listen:error", e)
                return@addSnapshotListener
            }

            for (dc in snapshots!!.documents) {
                Log.d("OrderRepo_Firestore3", "Document = ${dc.data.toString()}")
            }

            var pendingDataOrderIds = emptyList<Int>()
            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        Log.d("OrderRepo_Firestore4", "Entered new document addition")
                        order_id = dc.document.id
                        pendingDataOrderIds=pendingDataOrderIds.plus(order_id!!.toInt())



                    }
                    DocumentChange.Type.MODIFIED -> {
                        Log.d("OrderRepo_Firestore5", "Entered  document modifued ${dc.document.data}")
                        var order_id=dc.document.id
                        onOrderStatusModified(order_id!!.toInt(),dc.document["status"].toString().toInt(),false)
                    }
                    DocumentChange.Type.REMOVED ->
                        Log.d("OrderRepo_Firestore6", "Removed city: ${dc.document.data}")

                }
            }

            onNewOrderAdded(pendingDataOrderIds.minus(previousOrderIds))

        }


    }

    @SuppressLint("CheckResult")
    fun onNewOrderAdded(orderIdList: List<Int>) {
        Log.d(
            "OrderRepo_Firestore7",
            "entered on new order jwt = ${jwt_token!!}\norderid = $orderIdList"
        )//"Basic YXBwZDpmaXJlYmFzZXN1Y2tz"
var body = JSONObject().also {ob->
    ob.put("order_id_list",JSONArray().also { arr->
        orderIdList.forEach{
            arr.put(it)
        }
    })
}
    orderService.getOrdersFromOrderIds("JWT "+jwt_token!!,body = body).subscribeOn(Schedulers.io())
        .subscribe({
            Log.d("Firestore14", "data saved in room $it ")
        Log.d("OrderRepo_Firestore8", "code ${it.code()}")
        when (it.code()) {

            200 -> {
                it.body()!!.forEach { orderPojo ->

                    orderDao.deleteItemsWithOrderId(orderId = orderPojo.orderId)
                    orderDao.insertOrders(orderPojo.toOrderData(orderId = orderPojo.orderId))
                    orderDao.insertOrderItems(orderPojo.toItemData(orderId = orderPojo.orderId))


                    //remove incomplete order if present
                    var position = incomp_order_status_list.indexOfFirst { it.orderId == orderPojo.orderId }
                    if (position != -1)
                        incomp_order_status_list.removeAt(position)

                }
                    ui_status_subject.onNext(
                        UIState.SuccessStateFetchingOrders(
                            "Order$orderIdList recieved "/*,
                            orderId*/,
                            incompleteOrderList = incomp_order_status_list
                        )
                    )



            }

            in 400..499 -> {

                orderIdList.forEach {orderId->


                var position = incomp_order_status_list.indexOfFirst { it.orderId == orderId }
                if (position != -1) {
                    incomp_order_status_list.set(
                        position,
                        IncompleteOrderStatus(orderId, application.getString(R.string.status_try_again))
                    )
                    // incomp_order_status_subject.onNext(incomp_order_status_list)
                } else {
                    incomp_order_status_list.add(
                        IncompleteOrderStatus(
                            orderId,
                            application.getString(R.string.status_try_again)
                        )
                    )

                }}
                ui_status_subject
                    .onNext(
                        UIState.ErrorStateFetchingOrders(
                            "Error code: ${it.code()} "
                            /*,orderId*/, incomp_order_status_list
                        )
                    )

            }

            in 500..599 -> {

                orderIdList.forEach { orderId ->


                    var position = incomp_order_status_list.indexOfFirst { it.orderId == orderId }
                    if (position != -1) {
                        incomp_order_status_list.set(
                            position,
                            IncompleteOrderStatus(orderId, application.getString(R.string.status_try_again))
                        )
                        // incomp_order_status_subject.onNext(incomp_order_status_list)
                    } else {
                        incomp_order_status_list.add(
                            IncompleteOrderStatus(
                                orderId,
                                application.getString(R.string.status_try_again)
                            )
                        )

                    }
                }
                ui_status_subject.onNext(
                    UIState.ErrorStateFetchingOrders(
                        "Server error code: ${it.code()}",
                        /*orderId,*/
                        incomp_order_status_list
                    )
                )

            }

            else -> {
                orderIdList.forEach { orderId->


                var position = incomp_order_status_list.indexOfFirst { it.orderId ==    orderId }
                if (position != -1) {
                    incomp_order_status_list.set(
                        position,
                        IncompleteOrderStatus(  orderId, application.getString(R.string.status_try_again))
                    )
                    // incomp_order_status_subject.onNext(incomp_order_status_list)
                } else {
                    incomp_order_status_list.add(
                        IncompleteOrderStatus(
                            orderId,
                            application.getString(R.string.status_try_again)
                        )
                    )

                }
                }
                ui_status_subject.onNext(
                    UIState.ErrorStateFetchingOrders(
                        "Unknown error code: ${it.code()}"/*,
                        orderId*/,
                        incomp_order_status_list
                    )
                )


            }


        }

    }, {
        Log.d("OrderRepo_Firestore10", "error$it")
            orderIdList.forEach { orderId->


        var position = incomp_order_status_list.indexOfFirst { it.orderId == orderId }
        if (position != -1) {
            incomp_order_status_list.set(
                position,
                IncompleteOrderStatus(orderId, application.getString(R.string.status_try_again))
            )
            // incomp_order_status_subject.onNext(incomp_order_status_list)
        } else {
            incomp_order_status_list.add(
                IncompleteOrderStatus(
                    orderId,
                    application.getString(R.string.status_try_again)
                )
            )

        }}
        ui_status_subject.onNext(
            UIState.ErrorStateFetchingOrders(
                "Unknown error code: ${it}",
               /* orderId,*/
                incomp_order_status_list
            )
        )

    })

        /*orderService.getOrderFromOrderId(jwt = "JWT " + jwt_token!!, orderId = orderIdList)
            .subscribeOn(Schedulers.io()).subscribe({
                Log.d("Firestore14", "data saved in room $it ")
                Log.d("OrderRepo_Firestore8", "code ${it.code()}")
                when (it.code()) {

                    200 -> {
                        orderDao.deleteItemsWithOrderId(orderId = orderIdList.toInt())
                        orderDao.insertOrders(it.body()!!.toOrderData(orderId = orderIdList.toInt()))
                        orderDao.insertOrderItems(it.body()!!.toItemData(orderId = orderIdList.toInt()))

                        Log.d(
                            "OrderRepo_Firestore9",
                            "data saved in room   \n ${it.body()!!.status}, items ${it.body()!!.items} "
                        )

                        //remove incomplete order if present
                        var position = incomp_order_status_list.indexOfFirst { it.orderId == orderIdList.toInt() }
                        if (position != -1)
                            incomp_order_status_list.removeAt(position)

                        ui_status_subject.onNext(
                            UIState.SuccessStateFetchingOrders(
                                "Order$orderIdList recieved ",
                                orderId = orderIdList.toInt(),
                                incompleteOrderList = incomp_order_status_list
                            )
                        )


                    }

                    in 400..499 -> {

                        var position = incomp_order_status_list.indexOfFirst { it.orderId == orderIdList.toInt() }
                        if (position != -1) {
                            incomp_order_status_list.set(
                                position,
                                IncompleteOrderStatus(orderIdList.toInt(), application.getString(R.string.status_try_again))
                            )
                            // incomp_order_status_subject.onNext(incomp_order_status_list)
                        } else {
                            incomp_order_status_list.add(
                                IncompleteOrderStatus(
                                    orderIdList.toInt(),
                                    application.getString(R.string.status_try_again)
                                )
                            )

                        }
                        ui_status_subject
                            .onNext(
                                UIState.ErrorStateFetchingOrders(
                                    "Error code: ${it.code()} "
                                    , orderIdList.toInt(), incomp_order_status_list
                                )
                            )

                    }

                    in 500..599 -> {
                        var position = incomp_order_status_list.indexOfFirst { it.orderId == orderIdList.toInt() }
                        if (position != -1) {
                            incomp_order_status_list.set(
                                position,
                                IncompleteOrderStatus(orderIdList.toInt(), application.getString(R.string.status_try_again))
                            )
                            // incomp_order_status_subject.onNext(incomp_order_status_list)
                        } else {
                            incomp_order_status_list.add(
                                IncompleteOrderStatus(
                                    orderIdList.toInt(),
                                    application.getString(R.string.status_try_again)
                                )
                            )

                        }
                        ui_status_subject.onNext(
                            UIState.ErrorStateFetchingOrders(
                                "Server error code: ${it.code()}",
                                orderIdList.toInt(),
                                incomp_order_status_list
                            )
                        )

                    }

                    else -> {
                        var position = incomp_order_status_list.indexOfFirst { it.orderId == orderIdList.toInt() }
                        if (position != -1) {
                            incomp_order_status_list.set(
                                position,
                                IncompleteOrderStatus(orderIdList.toInt(), application.getString(R.string.status_try_again))
                            )
                            // incomp_order_status_subject.onNext(incomp_order_status_list)
                        } else {
                            incomp_order_status_list.add(
                                IncompleteOrderStatus(
                                    orderIdList.toInt(),
                                    application.getString(R.string.status_try_again)
                                )
                            )

                        }
                        ui_status_subject.onNext(
                            UIState.ErrorStateFetchingOrders(
                                "Unknown error code: ${it.code()}",
                                orderIdList.toInt(),
                                incomp_order_status_list
                            )
                        )


                    }


                }

            }, {
                Log.d("OrderRepo_Firestore10", "error$it")
                var position = incomp_order_status_list.indexOfFirst { it.orderId == orderIdList.toInt() }
                if (position != -1) {
                    incomp_order_status_list.set(
                        position,
                        IncompleteOrderStatus(orderIdList.toInt(), application.getString(R.string.status_try_again))
                    )
                    // incomp_order_status_subject.onNext(incomp_order_status_list)
                } else {
                    incomp_order_status_list.add(
                        IncompleteOrderStatus(
                            orderIdList.toInt(),
                            application.getString(R.string.status_try_again)
                        )
                    )

                }
                ui_status_subject.onNext(
                    UIState.ErrorStateFetchingOrders(
                        "Unknown error code: ${it}",
                        orderIdList.toInt(),
                        incomp_order_status_list
                    )
                )

            })*/


    }

    @SuppressLint("CheckResult")
    private fun onOrderStatusModified(orderId: Int, status: Int,isLoading:Boolean) {
        Log.d("OrderRepo1", " api ca;l sucees upadte status $orderId  s $status")
        orderDao.updateOrderStatusRoom(orderId, status, isLoading = isLoading).subscribeOn(Schedulers.io()).subscribe({
            Log.d("OrderRepo2", " on sucessupddate order Room $orderId  s $status")
        }, {

            Log.d("OrderRepo3", "Error upddate orderRoom $orderId  s $status $it")
        })

    }

    @SuppressLint("CheckResult")
    fun changeLoadingStatusRoom(orderId: Int,isLoading:Boolean) {
        Log.d("OrderRepo5", " api ca;l error upadte status, islOding change $orderId  ls $isLoading")
        orderDao.updateLoadingStatusRoom(orderId, isLoading = isLoading).subscribeOn(Schedulers.io()).subscribe({
            Log.d("OrderRepo6", " on sucessupddate loadin Room $orderId  s $isLoading")
        }, {

            Log.d("OrderRepo7", "Error upddate loadin orderRoom $orderId  s $isLoading $it")
        })

    }

    fun getUIStateFlowable(): Flowable<UIState> {
        return ui_status_subject.toFlowable(BackpressureStrategy.LATEST).doOnError { Log.e("OrderRepo", "Failed to convert into uiState") }
    }

    @SuppressLint("CheckResult")
    fun updateStatus(orderId: Int, status: Int) {
        val body = JsonObject().also {
            it.addProperty("new_status", status)
        }
        Log.d("OrderRepo_API2", body.toString())
        orderService.updateStatus("JWT " + jwt_token!!, body, orderId.toString()).subscribeOn(Schedulers.io())
            .subscribe({
                Log.d("OrderRepo_API15","update status code:${it.code()},id : $orderId, new stats $status" )

                when(it.code()){

                    200->
                    {
                        changeLoadingStatusRoom(orderId,isLoading = false)
                       // onOrderStatusModified(orderId,status,isLoading = false)
                        //display toast message
                    }

                    else ->
                    {
                        // show error message
                       changeLoadingStatusRoom(orderId,isLoading = false)
                    }


                }
            }, {

                Log.d("OrderRepo_API15","error apicall ,id : $orderId, new stats $status error $it" )

            })
    }

    @SuppressLint("CheckResult")
    fun declineOrder(orderId: Int) {


        orderService.declineOrder("JWT " + jwt_token!!, orderId).subscribeOn(Schedulers.io()).subscribe({
            Log.d("OrderRepo_API16","delcine order code:${it.code()},id : $orderId" )

            when(it.code()){

                200->
                {
                    onOrderStatusModified(orderId,4,isLoading = false)
                    //display toast message
                }

                else ->
                {
                    //onOrderStatusNotModified
                    changeLoadingStatusRoom(orderId,isLoading = false)
                }


            }
        }, {

            Log.d("OrderRepo_API17","error apicall ,id : $orderId, declineorder error $it" )

        })

    }


    // get accepted and ready orders from room
    fun getAcceptedOrdersRoom(): Flowable<List<ModifiedOrdersDataClass>> {
        return orderDao.getAllAcceptedOrders().subscribeOn(Schedulers.io())
            .flatMap {
                var list = it.sortedBy { it.orderId }
                var orderItemList = emptyList<ModifiedOrdersDataClass>()
                var itemList = emptyList<ChildDataClass>()
                for ((index, item) in list.iterator().withIndex()) {
                    itemList = itemList.plus(
                        ChildDataClass(
                            itemId = item.itemId,
                            price = item.price,
                            quantity = item.quantity,
                            itemName = item.name
                        )
                    )
                    if (!(index != list.size - 1 && list[index].orderId == list[index + 1].orderId)) {
                        orderItemList = orderItemList.plus(
                            ModifiedOrdersDataClass(
                                orderId = item.orderId,
                                status = item.status_order,
                                timestamp = item.timestamp.toString(),
                                otp = item.otp,
                                totalAmount = item.totalAmount,
                                items = itemList,
                                isLoading = item.is_loading

                            )
                        )
                        itemList = emptyList<ChildDataClass>()
                    }
                }
                return@flatMap Flowable.just(orderItemList)
            }.doOnError {
                Log.e("OrderRepo_API7", "Error in reading database = ${it.message.toString()}")
            }
    }

    fun getAllNewOrdersRoom(): Flowable<List<ModifiedOrdersDataClass>> {
        Log.d("OrderRepo_API8", "Entered to get orders")
        return orderDao.getAllNewOrders()
            .flatMap {
                Log.d("OrderRepo_API9", "Accepted Orders Recived = ${it.toString()}")
                var list = it.sortedBy { it.orderId }
                var orderItemList = emptyList<ModifiedOrdersDataClass>()
                var itemList = emptyList<ChildDataClass>()
                for ((index, item) in list.iterator().withIndex()) {
                    itemList = itemList.plus(
                        ChildDataClass(
                            itemId = item.itemId,
                            price = item.price,
                            quantity = item.quantity,
                            itemName = item.name
                        )
                    )
                    if (!(index != list.size - 1 && list[index].orderId == list[index + 1].orderId)) {
                        orderItemList = orderItemList.plus(
                            ModifiedOrdersDataClass(
                                orderId = item.orderId,
                                status = item.status_order,
                                timestamp = item.timestamp.toString(),
                                otp = item.otp,
                                totalAmount = item.totalAmount,
                                items = itemList,
                                isLoading = item.is_loading
                            )
                        )
                        itemList = emptyList<ChildDataClass>()
                    }
                }
                Log.d("OrderRepo_API10", "Returned From Repo = ${orderItemList.toString()}")
                return@flatMap Flowable.just(orderItemList)
            }.doOnError {
                Log.e("OrderRepo_API11", "Error in reading database = ${it.message.toString()}")
            }
    }


// returns orders with status "finish"
/*fun getFinishedOrdersFromRoom(): Flowable<List<OrderItemsData>> {
return orderDao.getAllFinishedOrdersRoom().subscribeOn(Schedulers.io())
   .flatMap {
       var orderList = emptyList<OrderItemsData>()
       it.forEach { ordersData ->
           orderDao.getItemsForOrder(ordersData.orderId)
               .doOnSuccess { itemList ->
                   orderList = orderList.plus(OrderItemsData(ordersData, itemList))
               }.doOnError {
                   Log.e(
                       "Testing Repo",
                       "Error in reading finished orders from room\nError = ${it.message.toString()}"
                   )
               }.subscribe()
       }
       Log.d("CheckAcceptedList", orderList.size.toString())
       return@flatMap Flowable.just(orderList)
   }.doOnError {
     //  Log.e("Finish1", "Error getting room data${it}")
   }
}*/

    //update room with earnings data
    fun updateEarningsData(): Completable {

        return earningsApiCall.subscribeOn(Schedulers.io())
            .doOnSuccess {

                var daywiseEarnings = emptyList<EarningData>()

                it.daywise.forEach { dayPojo ->

                    daywiseEarnings = daywiseEarnings.plus(dayPojo.toEarningData())
                }
                Log.d("OrderRepo_API12", daywiseEarnings.toString())
                earningDao.insertEarningData(daywiseEarnings)
            }.doOnError {
                Log.e("OrderRepo_API13", "error getting data from backend$it")
            }
            .ignoreElement()
    }

    private fun DayPojo.toEarningData(): EarningData {
        return EarningData(day = day, earnings = earnings)
    }

    // get earnings data From ROOM
    fun getdaywiseEarningRoom(): Flowable<List<EarningData>> {
        return earningDao.getDayWiseEarnings().subscribeOn(Schedulers.io()).doOnError {
            Log.e("OrderRepo_API14", "error getting data from room daywise$it")
        }
    }

    private fun OrdersPojo.toOrderData(orderId: Int): OrdersData {
        return OrdersData(
            orderId = orderId,
            status_order = status,
            isLoading = false,
            otp = otp,
            total_price = totalPrice,

            timestamp = 1564760765
        )
    }

    private fun OrdersPojo.toItemData(orderId: Int): List<ItemData> {

        var item = emptyList<ItemData>()

        items.forEach {
            item = item.plus(
                ItemData(
                    itemId = it.itemId,
                    price = it.unitPrice,
                    quantity = it.quantity,
                    orderId = orderId,
                    id = 0
                )
            )
        }
        return item
    }
}