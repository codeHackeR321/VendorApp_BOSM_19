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
    var dayWiseOrdersMap:LiveData<Map<String,List<ModifiedOrdersDataClass>>> =MutableLiveData()
    init{

        if (NetworkConnectivityCheck().checkIntenetConnection(context)) {
            orderRepository.updateEarningsData().observeOn(AndroidSchedulers.mainThread()).doOnComplete {
                getCompletedOrders()
                Log.d("Earnings1" , "Sucess API }")

            }.doOnError {
                Log.e("Earning2" , "Error in updating data from room \nError = ${it.message.toString()}")
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


   @SuppressLint("CheckResult")

   fun updateData(){

       orderRepository.getdaywiseEarningRoom().observeOn(AndroidSchedulers.mainThread()).doOnNext{
           Log.d("check1","called")
           var totalEarning:Long = 0
           it.forEach {
               Log.d("check1","earnings"+it.earnings)
               totalEarning = totalEarning + it.earnings.toLong()
           }
           (earnings as MutableLiveData<String>).postValue(totalEarning.toString())
       }.doOnError {
           Log.e("Error in COVM" , "Error in updating data = ${it.toString()}")
       }.subscribe()
   }

    @SuppressLint("CheckResult")
    fun getCompletedOrders(){

     //   var ordersList = emptyList<ModifiedOrdersDataClass>()
        var daywiseOrdersList=  emptyList<DayWiseOrdersDataClass>()
        orderRepository.getFinishedOrdersFromRoom().observeOn(AndroidSchedulers.mainThread()).subscribe({modifiedordersdtaclasslist->


           /* var date =SimpleDateFormat("dd'_'MM'_'yyyy").parse("05_06_2019")
            var sdf=SimpleDateFormat("dd-MM-yyyy")*/
var datewiseordersMap:Map<String,List<ModifiedOrdersDataClass>>

datewiseordersMap=modifiedordersdtaclasslist.groupBy { it.date }

/*
                var tempdate=SimpleDateFormat("dd-MM-yyyy").format(Date(order.order.timestamp.toString().toLong()*1000L))
*/
/*

            if (daywiseOrdersList.any{ it.date ==tempdate})
                    daywiseOrdersList.find { it.date==tempdate }!!.dayWiseorders.plus(ModifiedOrdersDataClass(
                        orderId = order.order.orderId.toString(),
                        otp = order.order.otp,
                        status = order.order.status.toString(),
                        totalAmount = order.order.total_price.toString(),
                        timestamp = order.order.timestamp.toString(),
                        items = itemList
                    ))
                else{
                       var tempList= emptyList<ModifiedOrdersDataClass>()
                    tempList=tempList.plus(ModifiedOrdersDataClass(
                        orderId = order.order.orderId.toString(),
                        otp = order.order.otp,
                        status = order.order.status.toString(),
                        totalAmount = order.order.total_price.toString(),
                        timestamp = order.order.timestamp.toString(),
                        items = itemList
                    ))
                   daywiseOrdersList= daywiseOrdersList.plus(DayWiseOrdersDataClass(tempdate,tempList))
                    Log.e("Completed3","daywiseLost${daywiseOrdersList.size} temp ${tempList.size}")
                }
*/

                //(dayWiseOrders as MutableLiveData<List<DayWiseOrdersDataClass>>).postValue(daywiseOrdersList)
            (dayWiseOrdersMap as MutableLiveData<Map<String,List<ModifiedOrdersDataClass>>>).postValue(datewiseordersMap)
        }
        , {
            Log.e(" check" , it.stackTrace.toString())
        })
    }




}