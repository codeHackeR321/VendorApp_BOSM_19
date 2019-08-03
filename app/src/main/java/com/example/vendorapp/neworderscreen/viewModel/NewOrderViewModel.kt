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
    fun getNewOrders():Flowable<List<ModifiedOrdersDataClass>> {

      return  orderRepo.getAllNewOrders()
    }

    /*fun doInitialFetch(){
        if (NetworkConnectivityCheck().checkIntenetConnection(context))
        {
            orderRepo.updateOrders().doOnComplete {
                Log.d("Testing NO ViewModel" , "Refresh Complete. Fetching Menu Items")
                menuRepo.updateMenu().doOnComplete {
                    Log.d("Testing NO ViewModel" , "Fetching Menu Items Complete. Fetching orders")
                    getNewOrders()
                }.doOnError {
                    Log.e("Testing NO ViewModel" , "Error in updating menu items = ${it.message.toString()}")
                    (error as MutableLiveData<String>).postValue("Error in database. Please try after some time")
                }.subscribe()
            }.doOnError {
                Log.e("Testing NO ViewModel" , "Error in updating = ${it.message.toString()}")
                (error as MutableLiveData<String>).postValue("Error in database. Please try after some time")
            }.subscribe()
        }
        else{
            (error as MutableLiveData<String>).postValue("Please check your internet connection and restart the app")
        }
    }*/

    @SuppressLint("CheckResult")
    fun observeUIState(): Flowable<UIState>{

      return  orderRepo.getUIStateFlowable()

    }


}