package com.example.vendorapp.neworderscreen.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.shared.UIState
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance
import com.example.vendorapp.shared.singletonobjects.repositories.MenuRepositoryInstance
import com.example.vendorapp.shared.utils.NetworkConnectivityCheck
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class NewOrderViewModel(val context : Context) : ViewModel(){

    var orders : LiveData<List<ModifiedOrdersDataClass>> = MutableLiveData()
    var orderRepo = OrderRepositoryInstance.getInstance(context)
    val menuRepo = MenuRepositoryInstance.getInstance(context)
    var errors : LiveData<String> = MutableLiveData()
    var orderUIStateNewOrder : LiveData<UIState> = MutableLiveData()
    var ui_status:LiveData<UIState> = MutableLiveData()
    var disposable : Disposable? = null
init {
    observeUIState()
}

    @SuppressLint("CheckResult")
    fun getNewOrders() {
        disposable?.dispose()
     disposable= orderRepo.getAllNewOrdersRoom().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe({
          Log.d("Firestore51", " Orders from room fetch ${it}")
          (orders as MutableLiveData<List<ModifiedOrdersDataClass>>).postValue(it)
      },{
          Log.e("Firestore50", "Error in Orders from room fetch ${it.message}")
          (errors as MutableLiveData<String>).postValue(it.toString())
      })
    }



    @SuppressLint("CheckResult")
    fun changeStatus(orderId:Int, status:Int,isLoading:Boolean) {
        orderRepo.changeLoadingStatusRoom(orderId,isLoading)
        orderRepo.updateStatus(orderId,status)
    }

    @SuppressLint("CheckResult")
    fun declineOrder(orderId:Int,isLoading: Boolean) {
        orderRepo.changeLoadingStatusRoom(orderId,isLoading)
        orderRepo.declineOrder(orderId)
    }

    fun fetchOrderAgain(orderId: Int){
      orderRepo.onNewOrderAdded(listOf(orderId))
    }

    @SuppressLint("CheckResult")
    fun doInitialFetch(){
        if (NetworkConnectivityCheck().checkIntenetConnection(context))
        {

                menuRepo.updateMenu().subscribe({
                    Log.d("NewOrderVM1" , "Fetching Menu Items Complete. Fetching orders")
                    //getNewOrders()
                },{
                    Log.d("NewOrderVM2" , "Error in updating menu items = ${it.message.toString()}")
                    (errors as MutableLiveData<String>).postValue("Error in database. Please try after some time")
                })

        }
        else{
            (errors as MutableLiveData<String>).postValue("Please check your internet connection and restart the app")
        }
    }

    @SuppressLint("CheckResult")
    fun observeUIState(){

        orderRepo.getUIStateFlowable().subscribeOn(Schedulers.io()).subscribe({
            ( orderUIStateNewOrder as MutableLiveData).postValue(it)
        },{
            Log.e("NewVM", "error flowablew accepetd ui syate")
        })

    }

    override fun onCleared() {
        Log.d("ViewModelnewe","Acce destroiyed")
        super.onCleared()
    }


}