package com.example.vendorapp.completedorderscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.completedorderscreen.view.adapters.DatesAdapter
import com.example.vendorapp.completedorderscreen.view.adapters.OrdersAdapterFragment
import com.example.vendorapp.completedorderscreen.viewModel.CompletedOrderViewModel
import com.example.vendorapp.completedorderscreen.viewModel.CompletedOrderViewModelFactory
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import kotlinx.android.synthetic.main.activity_completed_orders.*
import java.lang.Exception

class CompletedOrdersActivity : AppCompatActivity(), DatesAdapter.DateSelectedListener {

    private lateinit var nCompletedViewModel: CompletedOrderViewModel
    private var defaultDate="13"

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
        // fun to update expandable recycler view with datewise data
        setDayWiseData(defaultDate)
    }

    override fun OnDateSelected(date: String) {
      setDayWiseData(date)
    }

    private fun setDayWiseData(date: String){

        nCompletedViewModel.dayWiseOrders.observe(this, Observer {
           try {
                //date wise data supplied
               (orders_recycler.adapter as OrdersAdapterFragment).orders=it.find { dayWiseOrdersDataClass ->
                   dayWiseOrdersDataClass.date==date
               }!!.dayWiseorders

               //Check daywise data
               Log.d("CompletedDataCheck","data Check${it.find { dayWiseOrdersDataClass ->
                   dayWiseOrdersDataClass.date==date
               }!!.dayWiseorders.toString()}")
               (orders_recycler.adapter as OrdersAdapterFragment).notifyDataSetChanged()
           }
           catch (e :Exception){
               Log.d("CompletedError",e.toString())
               Toast.makeText(this@CompletedOrdersActivity,e.toString(),Toast.LENGTH_LONG).show()
           }

            Toast.makeText(this@CompletedOrdersActivity,"Date slected$date",Toast.LENGTH_LONG).show()

        })
    }
}
