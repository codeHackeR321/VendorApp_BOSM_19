package com.example.vendorapp.shared.singletonobjects.model

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.vendorapp.R
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.completedorderscreen.model.room.EarningDao
import com.example.vendorapp.loginscreen.view.UIState
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
import com.google.gson.JsonObject
import io.reactivex.*
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class OrderRepository(val application: Context) {

    private val orderDao: OrderDao
    private val orderApiCall: Single<List<OrdersPojo>>
    private val earningDao: EarningDao
    private val earningsApiCall: Single<EarningsPojo>
    private var jwt_token: String?=null
    private var vendor_id: String?=null
    private val orderService = RetrofitInstance.getRetroInstance()
    var ui_status_repo : Flowable<UIState> = Flowable.just(UIState.ShowInitialState)
    var ui_status_subject = BehaviorSubject.create<UIState>()

    private val db = FirebaseFirestore.getInstance()

    private var isNewOrderAdded=false

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
         jwt_token = sharedPref.getString(application.getString(R.string.saved_jwt), "")
         vendor_id = sharedPref.getString(application.getString(R.string.saved_vendor_id), "1")
        Log.d("Firestore99", "Vendor id = $vendor_id")

initFirestore()

    }

    fun initFirestore(){
        //receive order id from firestore
        var  order_id:String?=null
        db.collection("orders").whereEqualTo("vendorid", vendor_id!!.toInt()).addSnapshotListener { snapshots, e ->

            Log.d("Firestore98", "Snapshot = ${snapshots.toString()}")
            Log.d("Firestore97", "Error = ${e.toString()}")

            if (e != null) {
                Log.w("Sports", "listen:error", e)
                return@addSnapshotListener
            }

            for (dc in snapshots!!.documents) {
                Log.d("Firestore96", "Document = ${dc.data.toString()}")
            }

            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        Log.d("Firestore100", "Entered new document addition")
                       order_id=dc.document.id

                        onNewOrderAdded(order_id!!)

                    }
                    DocumentChange.Type.MODIFIED ->
                        Log.d("sports3", "Modified city: ${dc.document.data}")
                    DocumentChange.Type.REMOVED ->
                        Log.d("sports4", "Removed city: ${dc.document.data}")

                }
            }

        }



    }
    @SuppressLint("CheckResult")
     fun onNewOrderAdded(orderId: String){
        Log.d("Firestore10","entered on new order jwt = ${jwt_token!!}\norderid = $orderId")
         orderService.getOrderFromOrderId(jwt = "Basic YXBwZDpmaXJlYmFzZXN1Y2tz",orderId = orderId)
            .subscribeOn(Schedulers.io()).subscribe({Log.d("Firestore14","data saved in room $it ")
                 Log.d("Firestore9","code ${it.code()}")
                 when(it.code())
                 {

                     200 -> {

                         orderDao.insertOrders(it.body()!!.toOrderData(orderId = orderId.toInt()))
                         orderDao.insertOrderItems(it.body()!!.toItemData(orderId = orderId.toInt()))

                         var  ordersData= emptyList<OrdersData>()
                         ordersData=ordersData.plus(orderDao.getCheckNewOrders())
                         Log.d("Firestore3","data saved in room  order data $ordersData \n ${it.body()!!.status}, items ${it.body()!!.items} ")
                         ui_status_subject.onNext(UIState.SuccessState("Order$orderId recieved "))

                         /*Single.just(UIState.SuccessState("Data updated for Order Id: $orderId"))*/
                     }

                     in 400..499->
                         ui_status_subject.onNext(UIState.ErrorState("login error if 401 code: ${it.code()}"))


                     in 500..599->
                         ui_status_subject.onNext(UIState.ErrorState("Server error code: ${it.code()}"))


                     else ->
                         ui_status_subject.onNext(UIState.ErrorState("Unknown error code: ${it.code()}"))



                 }

                 },{
                 ui_status_subject.onNext(UIState.ErrorState("Unknown error code: ${it}"))


             })



}

    fun getUIStateFlowable(): Flowable<UIState>{
     return   ui_status_subject.toFlowable(BackpressureStrategy.LATEST)
    }
fun getOrderFromId(orderId: String): Flowable<OrdersData> {
return orderDao.getOrderById(orderId).subscribeOn(Schedulers.io())
}

fun updateStatus(orderId: String, status: String): Completable {
val body = JsonObject().also {
   it.addProperty("new_status", status)

}
Log.d("check", body.toString())
return orderService.updateStatus(body, orderId).doOnSuccess {
   Log.d("check", "${it.code()} : ${it.message()}")
}.ignoreElement()
}


fun updateStatusInRoom() {

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
                       status = item.status,
                       timestamp = item.timestamp.toString(),
                       otp = item.otp,
                       totalAmount = item.totalAmount,
                       items = itemList
                   )
               )
               itemList = emptyList<ChildDataClass>()
           }
       }
       return@flatMap Flowable.just(orderItemList)
   }.doOnError {
       Log.e("Testing Repo", "Error in reading database = ${it.message.toString()}")
   }
}

fun getAllNewOrders(): Flowable<List<ModifiedOrdersDataClass>> {
Log.d("Testing Repo", "Entered to get orders")
return orderDao.getAllNewOrders().subscribeOn(Schedulers.io())
   .flatMap {
       Log.d("Testing Repo", "Accepted Orders Recived = ${it.toString()}")
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
                       status = item.status,
                       timestamp = item.timestamp.toString(),
                       otp = item.otp,
                       totalAmount = item.totalAmount,
                       items = itemList
                   )
               )
               itemList = emptyList<ChildDataClass>()
           }
       }
       Log.d("Testing Repo", "Returned From Repo = ${orderItemList.toString()}")
       return@flatMap Flowable.just(orderItemList)
   }.doOnError {
       Log.e("Testing Repo", "Error in reading database = ${it.message.toString()}")
   }
}


// returns orders with status "finish"
/*fun getFinishedOrdersFromRoom(): Flowable<List<OrderItemsData>> {
return orderDao.getFinishOrders().subscribeOn(Schedulers.io())
   .flatMap {*//*
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
       return@flatMap Flowable.just(orderList)*//*
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
       Log.d("check", daywiseEarnings.toString())
       earningDao.insertEarningData(daywiseEarnings)
   }.doOnError {
       Log.e("Finish2", "error getting data from backend$it")
   }
   .ignoreElement()
}

private fun DayPojo.toEarningData(): EarningData {
return EarningData(day = day, earnings = earnings)
}

// get earnings data From ROOM
fun getdaywiseEarningRoom(): Flowable<List<EarningData>> {
return earningDao.getDayWiseEarnings().subscribeOn(Schedulers.io()).doOnError {
   Log.e("Finish3", "error getting data from room daywise$it")
}
}

fun updateOrders(): Completable {

return orderApiCall.subscribeOn(Schedulers.io())
   .doOnSuccess {
       /* Log.d("Testing Repo", "Api success with ${it.toString()}")
        var orders = emptyList<OrdersData>()
        var items = emptyList<ItemData>()

        it.forEach { ordersPojo ->

            orders = orders.plus(ordersPojo.toOrderData())
            items = items.plus(ordersPojo.toItemData())

        }
        Log.d("Testing Repo", "Orders Added = ${orders.toString()}")
        Log.d("Testing Repo", "Items added = ${items.toString()}")
        orderDao.deleteAllOrderItems()
        orderDao.insertOrders(orders)
        orderDao.insertOrderItems(items)*/
   }.doOnError {
       Log.e("Testing Repo", "Error in fetching data = ${it.message.toString()}")
   }
   .ignoreElement()
}

private fun OrdersPojo.toOrderData(orderId: Int): OrdersData {
return OrdersData(
   orderId = orderId,
   status = status,
   otp = "1234",
   total_price = total_price,
   timestamp = 1564760765
)
}

private fun OrdersPojo.toItemData(orderId: Int): List<ItemData> {

var item = emptyList<ItemData>()

items.forEach {
   item = item.plus(
       ItemData(
           itemId = it.id,
           price = it.unit_price,
           quantity = it.quantity,
           orderId = orderId,
           id = 0
       )
   )
}
return item
}
}