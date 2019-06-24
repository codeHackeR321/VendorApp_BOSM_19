package com.example.vendorapp.completedOrderScreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.completedOrderScreen.view.adapters.DatesAdapter
import com.example.vendorapp.completedOrderScreen.view.adapters.OrdersAdapterFragment
import com.example.vendorapp.completedOrderScreen.viewModel.CompletedOrderViewModel
import com.example.vendorapp.completedOrderScreen.viewModel.CompletedOrderViewModelFactory
import kotlinx.android.synthetic.main.activity_completed_orders.*

class CompletedOrdersActivity : AppCompatActivity(),DatesAdapter.DateSelectedListener {

    private lateinit var nCompletedViewModel:CompletedOrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_orders)

        nCompletedViewModel=ViewModelProviders.of(this,CompletedOrderViewModelFactory(this)).get(CompletedOrderViewModel::class.java)
        initialize()
        dates_recycler.adapter=DatesAdapter(this)
        (dates_recycler.adapter as DatesAdapter).dates = listOf("13","14","15","16")
        nCompletedViewModel.earnings.observe(this, Observer {
            earning.text=it
        })

    }

    fun initialize(){
         orders_recycler.adapter=OrdersAdapterFragment()
         nCompletedViewModel.updateData()
        nCompletedViewModel.orders.observe(this, Observer {
            (orders_recycler.adapter as OrdersAdapterFragment).orders=it
            (orders_recycler.adapter as OrdersAdapterFragment).notifyDataSetChanged()
        })
    }

    override fun OnDateSelected(date: String) {
        nCompletedViewModel.getOrdersForDate(date)
    }
}
