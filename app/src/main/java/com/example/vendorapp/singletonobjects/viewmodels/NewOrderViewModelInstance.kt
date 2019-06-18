package com.example.vendorapp.singletonobjects.viewmodels

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModel

class NewOrderViewModelInstance() {

    companion object {

        var NewOrderViewModel : NewOrderViewModel? = null

        @Synchronized fun getInstance(fragment: Fragment) : NewOrderViewModel {

            if (NewOrderViewModel == null)
            {
                NewOrderViewModel = ViewModelProviders.of(fragment).get(com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModel::class.java)
            }

            return NewOrderViewModel!!

        }

    }

}