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
    var incomp_order_status_list= emptyList<IncompleteOrderStatus>()
    var arrayList=ArrayList<IncompleteOrderStatus>()

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
         jwt_token = sharedPref.getString(application.getString(R.string.saved_jwt), application.getString(R.string.default_jwt_value))
         vendor_id = sharedPref.getString(application.getString(R.string.saved_vendor_id), application.getString(R.string.default_vendor_id))
        Log.d("OrderRepo_API1", "Vendor id = $vendor_id")

initFirestore()

    }

    fun initFirestore(){
        //receive order id from firestore
        var  order_id:String?=null
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

            for (dc in snapshots!!.documentChanges) {
                when (dc.type) {
                    DocumentChange.Type.ADDED -> {
                        Log.d("OrderRepo_Firestore4", "Entered new document addition")
                       order_id=dc.document.id

                        onNewOrderAdded(order_id!!)

                    }
                    DocumentChange.Type.MODIFIED ->
                    {
                        Log.d("OrderRepo_Firestore5", "Entered  document modifued ${dc.document.data}")
                        var order_id=dc.document.id
                        onOrderStatusModified(order_id!!.toInt(),dc.document["status"].toString().toInt())
                    }
                    DocumentChange.Type.REMOVED ->
                        Log.d("OrderRepo_Firestore6", "Removed city: ${dc.document.data}")

                }
            }

        }



    }
    @SuppressLint("CheckResult")
     fun onNewOrderAdded(orderId: String){
        Log.d("OrderRepo_Firestore7","entered on new order jwt = ${jwt_token!!}\norderid = $orderId")//"Basic YXBwZDpmaXJlYmFzZXN1Y2tz"
         orderService.getOrderFromOrderId(jwt = "JWT "+jwt_token!!,orderId = orderId)
            .subscribeOn(Schedulers.io()).subscribe({Log.d("Firestore14","data saved in room $it ")
                 Log.d("OrderRepo_Firestore8","code ${it.code()}")
                 when(it.code())
                 {

                     200 -> {
                         orderDao.deleteItemsWithOrderId(orderId = orderId.toInt())
                         orderDao.insertOrders(it.body()!!.toOrderData(orderId = orderId.toInt()))
                         orderDao.insertOrderItems(it.body()!!.toItemData(orderId = orderId.toInt()))

                         Log.d("OrderRepo_Firestore9","data saved in room   \n ${it.body()!!.status}, items ${it.body()!!.items} ")
                         //remove if incomnplte order present
                         if(incomp_order_status_list.find{it.orderId==orderId.toInt()}!=null){
                           incomp_order_status_list=  incomp_order_status_list.minus(incomp_order_status_list.find{it.orderId==orderId.toInt()}!!)
                           //  incomp_order_status_subject.onNext(incomp_order_status_list)
                         }


                         ui_status_subject.onNext(UIState.SuccessStateFetchingOrders("Order$orderId recieved ",orderId = orderId.toInt(),incompleteOrderList = incomp_order_status_list))


                         /*Single.just(UIState.SuccessState("Data updated for Order Id: $orderId"))*/
                     }

                     in 400..499-> {
                         if(incomp_order_status_list.find{it.orderId==orderId.toInt()}!=null){
                             incomp_order_status_list=incomp_order_status_list.minus(incomp_order_status_list.find{it.orderId==orderId.toInt()}!!)
                            incomp_order_status_list=incomp_order_status_list.plus(IncompleteOrderStatus(orderId.toInt(),application.getString(R.string.status_try_again)))
                            // incomp_order_status_subject.onNext(incomp_order_status_list)
                         }
                         else{
                             incomp_order_status_list=incomp_order_status_list.plus(IncompleteOrderStatus(orderId.toInt(),application.getString(R.string.status_try_again)))

                         }
                         ui_status_subject
                             .onNext(UIState.ErrorStateFetchingOrders("Error code: ${it.code()} "
                                 ,orderId.toInt(),incomp_order_status_list))

                     }

                     in 500..599-> {
                         if(incomp_order_status_list.find{it.orderId==orderId.toInt()}!=null){
                             incomp_order_status_list=incomp_order_status_list.minus(incomp_order_status_list.find{it.orderId==orderId.toInt()}!!)
                             incomp_order_status_list=incomp_order_status_list.plus(IncompleteOrderStatus(orderId.toInt(),application.getString(R.string.status_try_again)))
                           //  incomp_order_status_subject.onNext(incomp_order_status_list)
                         }
                         ui_status_subject.onNext(UIState.ErrorStateFetchingOrders("Server error code: ${it.code()}",orderId.toInt(),incomp_order_status_list))

                     }

                     else -> {
                         if(incomp_order_status_list.find{it.orderId==orderId.toInt()}!=null){
                             incomp_order_status_list=incomp_order_status_list.minus(incomp_order_status_list.find{it.orderId==orderId.toInt()}!!)
                             incomp_order_status_list=incomp_order_status_list.plus(IncompleteOrderStatus(orderId.toInt(),application.getString(R.string.status_try_again)))

                             //incomp_order_status_subject.onNext(incomp_order_status_list)
                         }
                         ui_status_subject.onNext(UIState.ErrorStateFetchingOrders("Unknown error code: ${it.code()}",orderId.toInt(),incomp_order_status_list))


                     }


                 }

                 },{
                 Log.d("OrderRepo_Firestore10","error$it")
                 if(incomp_order_status_list.find{it.orderId==orderId.toInt()}!=null){
                     incomp_order_status_list=incomp_order_status_list.minus(incomp_order_status_list.find{it.orderId==orderId.toInt()}!!)
                     incomp_order_status_list=incomp_order_status_list.plus(IncompleteOrderStatus(orderId.toInt(),application.getString(R.string.status_try_again)))

                  //   incomp_order_status_subject.onNext(incomp_order_status_list)
                 }
                 ui_status_subject.onNext(UIState.ErrorStateFetchingOrders("Unknown error code: ${it}",orderId.toInt(),incomp_order_status_list))

             })



}

 @SuppressLint("CheckResult")
 private fun onOrderStatusModified(orderId: Int, status: Int){
     Log.d("OrderRepo_Firestore11"," function caledupddate order $orderId  s $status")
        orderDao.updateOrderStatusRoom(orderId,status).subscribeOn(Schedulers.io()).subscribe({
            Log.d("OrderRepo_Firestore12"," on sucessupddate order $orderId  s $status")
        },{

            Log.d("OrderRepo_Firestore13","Error upddate order $orderId  s $status $it")
        })

 }
    fun getUIStateFlowable(): Flowable<UIState>{
     return   ui_status_subject.toFlowable(BackpressureStrategy.LATEST)
    }




fun updateStatus(orderId: Int, status: Int): Completable {
val body = JsonObject().also {
   it.addProperty("new_status", status)

}
Log.d("OrderRepo_API2", body.toString())
return orderService.updateStatus("JWT "+jwt_token!!,body, orderId.toString()).doOnSuccess {
   Log.d("OrderRepo_API3", "${it.code()} : ${it.message()}, body :${it.body()}")
}.doOnError {
  Log.d("OrderRepo_API4","error change staus $it")
}.ignoreElement()
}

//decline  an order
    fun declineOrder(orderId: Int): Completable {


        return orderService.declineOrder("JWT "+jwt_token!!, orderId).doOnSuccess {
            Log.d("OrderRepo_API5", "${it.code()} : ${it.message()}, body :${it.body()}")
        }.doOnError {
            Log.d("OrderRepo_API6","error decline $it")
        }.ignoreElement()
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
   status = status,
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