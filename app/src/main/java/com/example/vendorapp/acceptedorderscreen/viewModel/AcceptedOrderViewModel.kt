package com.example.vendorapp.acceptedorderscreen.viewModel

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.UIState
import com.example.vendorapp.shared.singletonobjects.repositories.OrderRepositoryInstance
import io.reactivex.Flowable
import io.reactivex.schedulers.Schedulers

class AcceptedOrderViewModel(context : Context) : ViewModel(){


    var  acceptedOrders : LiveData<List<ModifiedOrdersDataClass>> = MutableLiveData()
    var orderRepo = OrderRepositoryInstance.getInstance(context)
    var error : LiveData<String> = MutableLiveData()

    @SuppressLint("CheckResult")
    fun getAcceptedOrders(){
        orderRepo.getAcceptedOrdersRoom().doOnNext {
            Log.d("Testing AO VM" , "Entered observer with list = ${it.toString()}")
            (acceptedOrders as MutableLiveData<List<ModifiedOrdersDataClass>>).postValue(it)
        }.doOnError {
            Log.e("Testing AO VM" , "Error in reading new orders from database")
            (error as MutableLiveData<String>).postValue("Error in database. Please try after some time")
        }.subscribe({},{
            Log.e("Testing AO VM" , "Error in reading new orders from database")
            (error as MutableLiveData<String>).postValue("Error in database. Please try after some time")
        })
    }


    @SuppressLint("CheckResult")
    fun changeStatus(orderId:Int, status:Int,isLoading:Boolean) {
        orderRepo.changeLoadingStatusRoom(orderId,isLoading)
        orderRepo.updateStatus(orderId,status)
    }

    @SuppressLint("CheckResult")
    fun observeUIState(): Flowable<UIState> {
        return  orderRepo.getUIStateFlowable().doOnError { Log.e("TAG", "UI State observer error hu") }
    }

}

