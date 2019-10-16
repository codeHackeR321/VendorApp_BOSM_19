package com.example.vendorapp.homeactivity.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vendorapp.acceptedorderscreen.viewModel.AcceptedOrderViewModel

class MainScreenViewModelFactory (val context : Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AcceptedOrderViewModel(context) as T
    }

}