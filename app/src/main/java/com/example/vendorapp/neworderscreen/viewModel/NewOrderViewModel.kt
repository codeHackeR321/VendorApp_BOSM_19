package com.example.vendorapp.neworderscreen.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.vendorapp.loginscreen.view.UIState
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import com.example.vendorapp.shared.singletonobjects.repositories.MenuRepositoryInstance
import com.example.vendorapp.shared.utils.NetworkConnectivityCheck
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class NewOrderViewModel(val context : Context) : ViewModel(){

    var orders : LiveData<List<ModifiedOrdersDataClass>> = MutableLiveData()
    var orderRepo = OrderRepositoryInstance.getInstance(context)
    val menuRepo = MenuRepositoryInstance.getInstance(context)
    var error : LiveData<String> = MutableLiveData()
    var ui_status:LiveData<UIState> = MutableLiveData()


    @SuppressLint("CheckResult")
    fun getNewOrders() {

      orderRepo.getAllNewOrdersRoom().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).doOnNext {
          Log.d("Firestore51", " Orders from room fetch ${it}")
          (orders as MutableLiveData<List<ModifiedOrdersDataClass>>).postValue(it)
      }.doOnError {
          Log.e("Firestore50", "Error in Orders from room fetch ${it.message}")
      }.subscribe()
    }



    @SuppressLint("CheckResult")
    fun changeStatus(orderId:Int, status:Int) {
        orderRepo.updateStatus(orderId,status).subscribeOn(Schedulers.io()).subscribe({
            Log.d("Status3","staus chenged ")
        },{
            Log.d("Status2","error viewmodel change stuas$it")
        })
    }

    @SuppressLint("CheckResult")
    fun declineOrder(orderId:Int) {
        orderRepo.declineOrder(orderId).subscribeOn(Schedulers.io()).subscribe({
            Log.d("Status4","declines ")
        },{
            Log.d("Status5","error viewmodel decline order$it")
        })
    }

    fun fetchOrderAgain(orderId: Int){
orderRepo.onNewOrderAdded(orderId = orderId.toString())
    }

    fun doInitialFetch(){
        if (NetworkConnectivityCheck().checkIntenetConnection(context))
        {

                menuRepo.updateMenu().doOnComplete {
                    Log.d("NewOrderVM1" , "Fetching Menu Items Complete. Fetching orders")
                    //getNewOrders()
                }.doOnError {
                    Log.d("NewOrderVM2" , "Error in updating menu items = ${it.message.toString()}")
                    (error as MutableLiveData<String>).postValue("Error in database. Please try after some time")
                }.subscribe()

        }
        else{
            (error as MutableLiveData<String>).postValue("Please check your internet connection and restart the app")
        }
    }

    @SuppressLint("CheckResult")
    fun observeUIState(): Flowable<UIState>{

      return  orderRepo.getUIStateFlowable()

    }


}