package com.example.vendorapp.completedorderscreen.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
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
import kotlinx.android.synthetic.main.fragment_fra_new_order.*
import java.lang.Exception

class CompletedOrdersActivity : AppCompatActivity(), DatesAdapter.DateSelectedListener {

    private lateinit var nCompletedViewModel: CompletedOrderViewModel
    private var defaultDate="13"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_orders)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        nCompletedViewModel=ViewModelProviders.of(this,CompletedOrderViewModelFactory(this)).get(CompletedOrderViewModel::class.java)
        initialize()
        dates_recycler.adapter=DatesAdapter(this)
        (dates_recycler.adapter as DatesAdapter).dates = listOf("13","14","15","16")

        nCompletedViewModel.earnings.observe(this, Observer {
            earning.text= String.format(resources.getString(R.string.new_order_total_amount),it)
        })

        nCompletedViewModel.tearning.observe(this, Observer {
            t_earn_val.text= String.format(resources.getString(R.string.new_order_total_amount),it)
        })

        nCompletedViewModel.error.observe(this , Observer {
            Toast.makeText(this , it , Toast.LENGTH_LONG).show()
            if (progBar_new_order_screen.isVisible && it.isNotEmpty()) {
                progBar_new_order_screen.visibility = View.INVISIBLE
                window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

    }

    fun initialize(){
         orders_recycler.adapter=OrdersAdapterFragment()
         nCompletedViewModel.updateData()
        nCompletedViewModel.getEarningsForDate("Day1")
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


            Toast.makeText(this@CompletedOrdersActivity,"Date selected$date",Toast.LENGTH_LONG).show()

        })
        when(date){
            "13" -> nCompletedViewModel.getEarningsForDate("Day1")
            "14" -> nCompletedViewModel.getEarningsForDate("Day2")
            "15" -> nCompletedViewModel.getEarningsForDate("Day3")
            "16" -> nCompletedViewModel.getEarningsForDate("Day4")
        }

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId)
        {
            android.R.id.home ->
                finish()
        }
        return true
    }
}
