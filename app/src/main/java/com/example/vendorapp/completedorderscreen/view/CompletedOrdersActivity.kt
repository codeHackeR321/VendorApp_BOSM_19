package com.example.vendorapp.completedOrderScreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.completedOrderScreen.viewModel.CompletedOrderViewModel
import com.example.vendorapp.completedOrderScreen.viewModel.CompletedOrderViewModelFactory

class CompletedOrdersActivity : AppCompatActivity() {

    private lateinit var nCompletedViewModel:CompletedOrderViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_orders)
        nCompletedViewModel=ViewModelProviders.of(this,CompletedOrderViewModelFactory()).get(CompletedOrderViewModel::class.java)

    }
}
