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
import com.example.vendorapp.acceptedorderscreen.view.adapters.AdapterForFragment
import com.example.vendorapp.completedorderscreen.view.adapters.DatesAdapter
import com.example.vendorapp.completedorderscreen.view.adapters.OrdersAdapterFragment
import com.example.vendorapp.completedorderscreen.viewModel.CompletedOrderViewModel
import com.example.vendorapp.completedorderscreen.viewModel.CompletedOrderViewModelFactory
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import kotlinx.android.synthetic.main.activity_completed_orders.*
import kotlinx.android.synthetic.main.fragment_fra_accepted_order.*
import kotlinx.android.synthetic.main.fragment_fra_new_order.*
import java.lang.Exception

class CompletedOrdersActivity : AppCompatActivity(), DatesAdapter.DateSelectedListener {

    private lateinit var nCompletedViewModel: CompletedOrderViewModel
    private var defaultDate = "13"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_completed_orders)

        showLoadingStateActivity()

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        nCompletedViewModel = ViewModelProviders.of(this, CompletedOrderViewModelFactory(this)).get(CompletedOrderViewModel::class.java)

        initialize()

    }

    fun initialize() {
        //initialise recycler vies and adapter
        orders_recycler.adapter = OrdersAdapterFragment()
        dates_recycler.adapter=DatesAdapter(defaultDate,this)

        nCompletedViewModel.earningData.observe(this , Observer {
            Log.d("Testing Accepted View" , "Entered observer for Accepted orders with list = ${it.toString()}")
            (dates_recycler.adapter as DatesAdapter).earningData = it
            (dates_recycler.adapter as DatesAdapter).notifyDataSetChanged()
            removeLoadingStateActivity()
        })

        nCompletedViewModel.totalEarning.observe(this, Observer {
            earning.text = it
        })

        nCompletedViewModel.error.observe(this, Observer {
            Toast.makeText(this, it, Toast.LENGTH_LONG).show()
            Log.d("Check23", "efh$it")
            removeLoadingStateActivity()
        })

        //fetch data from room
        nCompletedViewModel.getEarningData()

        nCompletedViewModel.updateData()

        // fun to update expandable recycler view with datewise data
        setDayWiseData(defaultDate)

    }

    override fun OnDateSelected(date: String) {
        setDayWiseData(date)
    }

    private fun setDayWiseData(date: String) {

        nCompletedViewModel.dayWiseOrdersMap.observe(this, Observer {
            try {
                //date wise data supplied
                // null agar hua tio dikkat nhi hobni chhiye shayd
                (orders_recycler.adapter as OrdersAdapterFragment).orders = it[date]!!

                //Check daywise data
                Log.d("CompletedDataCheck", "data Check${it[date]}!!.dayWiseorders.toString()}")
                (orders_recycler.adapter as OrdersAdapterFragment).notifyDataSetChanged()
                (dates_recycler.adapter as DatesAdapter).notifyDataSetChanged()

            } catch (e: Exception) {
                Log.d("CompletedError", e.toString())
                Toast.makeText(this@CompletedOrdersActivity, e.toString(), Toast.LENGTH_LONG).show()
            }


            Toast.makeText(this@CompletedOrdersActivity, "Date selected$date", Toast.LENGTH_LONG).show()

        })


       /* nCompletedViewModel.dayWiseOrders.observe(this, Observer {
            try {
                //date wise data supplied
                (orders_recycler.adapter as OrdersAdapterFragment).orders = it.find { dayWiseOrdersDataClass ->
                    dayWiseOrdersDataClass.date == date
                }!!.dayWiseorders

                //Check daywise data
                Log.d("CompletedDataCheck", "data Check${it.find { dayWiseOrdersDataClass ->
                    dayWiseOrdersDataClass.date == date
                }!!.dayWiseorders.toString()}")
                (orders_recycler.adapter as OrdersAdapterFragment).notifyDataSetChanged()
                (dates_recycler.adapter as DatesAdapter).notifyDataSetChanged()

            } catch (e: Exception) {
                Log.d("CompletedError", e.toString())
                Toast.makeText(this@CompletedOrdersActivity, e.toString(), Toast.LENGTH_LONG).show()
            }


            Toast.makeText(this@CompletedOrdersActivity, "Date selected$date", Toast.LENGTH_LONG).show()

        })*/



    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home ->
                finish()
        }
        return true
    }

    private fun removeLoadingStateActivity() {
        if (prog_bar_completed_order_screen.isVisible) {
            prog_bar_completed_order_screen.visibility = View.INVISIBLE
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }

    private fun showLoadingStateActivity() {
        if (!prog_bar_completed_order_screen.isVisible) {
            prog_bar_completed_order_screen.visibility = View.VISIBLE
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }
}
