package com.example.vendorapp.neworderscreen.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.vendorapp.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass

class NewOrderViewModel() : ViewModel(){

    var orders : LiveData<List<OrdersPojo>> = MutableLiveData()

}