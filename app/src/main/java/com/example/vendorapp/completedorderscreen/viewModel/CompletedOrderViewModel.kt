package com.example.vendorapp.completedorderscreen.viewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.dataclasses.DayWiseOrdersDataClass
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import com.example.vendorapp.shared.singletonobjects.model.OrderRepository
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance.Companion.orderRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class CompletedOrderViewModel(context:Context) :ViewModel() {

   // var orders:LiveData<List<ModifiedOrdersDataClass>> =MutableLiveData()
    var dayWiseOrders:LiveData<List<DayWiseOrdersDataClass>> =MutableLiveData()
    var earnings:LiveData<String> = MutableLiveData()
    var orderRepository:OrderRepository= OrderRepositoryInstance.getInstance(context)


    init{

            orderRepository.updateEarningsData().observeOn(AndroidSchedulers.mainThread()).doOnComplete {
                getCompletedOrders()
            }.subscribe()

    }

   fun updateData(){

       orderRepository.getdaywiseEarningRoom().observeOn(AndroidSchedulers.mainThread()).subscribe({
           Log.d("check1","called")
           var totalEarning:Long = 0
           it.forEach {
               Log.d("check1",it.earnings)
               totalEarning = totalEarning + it.earnings.toLong()
           }
           Log.d("check1",totalEarning.toString())
           (earnings as MutableLiveData<String>).postValue(totalEarning.toString())
   })
   }

    fun getCompletedOrders(){

     //   var ordersList = emptyList<ModifiedOrdersDataClass>()
        var daywiseOrdersList=  emptyList<DayWiseOrdersDataClass>()
        orderRepository.getFinishedOrdersFromRoom().observeOn(AndroidSchedulers.mainThread()).doOnNext { orderList ->
            orderList.forEach {order ->
                var itemList = emptyList<ChildDataClass>()
                for (item in order.items) {
                    itemList = itemList.plus(
                        ChildDataClass(
                            itemName = item.name,
                            itemId = item.itemId,
                            price = item.price,
                            quantity = item.quantity
                        )
                    )
                }
                /*ordersList = ordersList.plus(ModifiedOrdersDataClass(
                    orderId = order.order.orderId,
                    otp = order.order.otp,
                    status = order.order.status,
                    totalAmount = order.order.totalAmount,
                    timestamp = order.order.timestamp.toString(),
                    items = itemList
                ))*/

                var tempdate=SimpleDateFormat("dd").format(Date(order.order.timestamp.toString().toLong()*1000L))
                if (daywiseOrdersList.any{ it.date ==tempdate})
                    daywiseOrdersList.find { it.date==tempdate }!!.dayWiseorders.plus(ModifiedOrdersDataClass(
                        orderId = order.order.orderId,
                        otp = order.order.otp,
                        status = order.order.status,
                        totalAmount = order.order.totalAmount,
                        timestamp = order.order.timestamp.toString(),
                        items = itemList
                    ))
                else{
                       var tempList= emptyList<ModifiedOrdersDataClass>()
                    tempList=tempList.plus(ModifiedOrdersDataClass(
                        orderId = order.order.orderId,
                        otp = order.order.otp,
                        status = order.order.status,
                        totalAmount = order.order.totalAmount,
                        timestamp = order.order.timestamp.toString(),
                        items = itemList
                    ))
                   daywiseOrdersList= daywiseOrdersList.plus(DayWiseOrdersDataClass(tempdate,tempList))
                    Log.e("Completed3","daywiseLost${daywiseOrdersList.size} temp ${tempList.size}")
                }



                //Log.e("Completed2","daywiseordeLost${daywiseOrdersList.size} ordersLost${ordersList.size}\n${daywiseOrdersList.toString()}")
                //(orders as MutableLiveData<List<ModifiedOrdersDataClass>>).postValue(ordersList)
                (dayWiseOrders as MutableLiveData<List<DayWiseOrdersDataClass>>).postValue(daywiseOrdersList)
            }
        }.doOnError {
            Log.e(" check" , it.stackTrace.toString())
        }.subscribe()
    }




}