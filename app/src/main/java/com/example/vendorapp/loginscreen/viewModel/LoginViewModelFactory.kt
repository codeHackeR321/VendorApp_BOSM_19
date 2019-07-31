package com.example.vendorapp.loginscreen.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModel




class  LoginViewModelFactory(val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LoginViewModel(context) as T
    }

}