package com.example.vendorapp.completedorderscreen.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.dataclasses.DayWiseOrdersDataClass
import com.example.vendorapp.shared.dataclasses.roomClasses.EarningData
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import com.example.vendorapp.shared.singletonobjects.model.OrderRepository
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance.Companion.orderRepository
import com.example.vendorapp.shared.utils.NetworkConnectivityCheck
import io.reactivex.android.schedulers.AndroidSchedulers
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

class CompletedOrderViewModel(context:Context) :ViewModel() {

   // var orders:LiveData<List<ModifiedOrdersDataClass>> =MutableLiveData()
    var dayWiseOrders:LiveData<List<DayWiseOrdersDataClass>> =MutableLiveData()
    var earnings:LiveData<String> = MutableLiveData()
    var orderRepository:OrderRepository= OrderRepositoryInstance.getInstance(context)
    var totalEarning:LiveData<String> =MutableLiveData()
    var error : LiveData<String> = MutableLiveData()
    var earningData:LiveData<List<EarningData>> = MutableLiveData()

    init{

        if (NetworkConnectivityCheck().checkIntenetConnection(context)) {
            orderRepository.updateEarningsData().observeOn(AndroidSchedulers.mainThread()).doOnComplete {
                getCompletedOrders()
            }.doOnError {
                Log.e("Testing Earnings VM" , "Error in updating data from room \nError = ${it.message.toString()}")
            }.subscribe()
        } else {
            (error as MutableLiveData<String>).postValue("Please check your internet connection and restart the app")
        }

    }


    @SuppressLint("CheckResult")
    fun getEarningData(){

        orderRepository.getdaywiseEarningRoom().observeOn(AndroidSchedulers.mainThread()).subscribe(
            {
                Log.d("EarningData", "dd"+it.toString())
                var totalEarnings: Long=0
                for (i in 0 until it.size)
                {
                    totalEarnings=totalEarnings+it[i].earnings.toLong()
                }
                (earningData as MutableLiveData<List<EarningData>>).postValue(it)
                (totalEarning as MutableLiveData<String>).postValue(totalEarnings.toString())
            },{
                Log.e("error",it.message)
            }
        )
    }


/*   @SuppressLint("CheckResult")

   fun updateData(){

       orderRepository.getdaywiseEarningRoom().observeOn(AndroidSchedulers.mainThread()).doOnNext{
           Log.d("check1","called")
           var totalEarning:Long = 0
           it.forEach {

               Log.d("check1",it.earnings)
               totalEarning = totalEarning + it.earnings.toLong()
           }
           (earnings as MutableLiveData<String>).postValue(totalEarning.toString())
       }.doOnError {
           Log.e("Error in COVM" , "Error in updating data = ${it.toString()}")
       }.subscribe()
   }*/

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

                (dayWiseOrders as MutableLiveData<List<DayWiseOrdersDataClass>>).postValue(daywiseOrdersList)
            }
        }.doOnError {
            Log.e(" check" , it.stackTrace.toString())
        }.subscribe()
    }




}