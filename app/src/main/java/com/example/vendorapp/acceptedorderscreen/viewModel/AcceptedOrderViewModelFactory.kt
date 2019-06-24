package com.example.vendorapp.acceptedorderscreen.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AcceptedOrderViewModelFactory(val context : Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AcceptedOrderViewModel(context) as T
    }

}