package com.example.vendorapp.completedorderscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.completedorderscreen.view.adapters.DatesAdapter
import com.example.vendorapp.completedorderscreen.viewModel.CompletedOrderViewModel
import com.example.vendorapp.completedorderscreen.viewModel.CompletedOrderViewModelFactory
import kotlinx.android.synthetic.main.activity_completed_orders.*

class CompletedOrdersActivity : AppCompatActivity(), DatesAdapter.DateSelectedListener {

    private lateinit var nCompletedViewModel: CompletedOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_orders)
        nCompletedViewModel=ViewModelProviders.of(this, CompletedOrderViewModelFactory(this.applicationContext)).get(
            CompletedOrderViewModel::class.java)
        initialize()
        dates_recycler.adapter= DatesAdapter(this)
        (dates_recycler.adapter as DatesAdapter).dates
        nCompletedViewModel.earnings.observe(this, Observer {
            earning.text=it
        })

    }

    fun initialize(){

    }

    override fun OnDateSelected(date: String) {
        nCompletedViewModel.getOrdersForDate(date)
    }
}
