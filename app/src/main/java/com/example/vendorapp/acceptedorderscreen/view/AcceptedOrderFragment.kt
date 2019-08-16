package com.example.vendorapp.acceptedorderscreen.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.acceptedorderscreen.view.adapters.AdapterForFragment
import com.example.vendorapp.acceptedorderscreen.viewModel.AcceptedOrderViewModel
import com.example.vendorapp.acceptedorderscreen.viewModel.AcceptedOrderViewModelFactory
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import kotlinx.android.synthetic.main.fragment_fra_accepted_order.*
import kotlinx.android.synthetic.main.fragment_fra_new_order.*


class AcceptedOrderFragment : Fragment(), ListenerRecyViewButtonClick {


    private val viewModel by lazy {
        ViewModelProviders.of(this, AcceptedOrderViewModelFactory(this.context!!))
            .get(AcceptedOrderViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fra_accepted_order, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeView()
        super.onViewCreated(view, savedInstanceState)

    }

    fun initializeView() {
showLoadingStateFragment()
        recycler_accepted_order_screen.adapter = AdapterForFragment(this)
        viewModel.acceptedOrders.observe(this, Observer {
            Log.d("Testing Accepted View", "Entered observer for Accepted orders with list = ${it.toString()}")
            removeLoadingStateFragment()
            (recycler_accepted_order_screen.adapter as AdapterForFragment).orders = it
            (recycler_accepted_order_screen.adapter as AdapterForFragment).notifyDataSetChanged()
        })

        viewModel.error.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            removeLoadingStateFragment()
        })

        viewModel.getAcceptedOrders()

    }


    override fun buttonClicked(orderId: Int, status: Int) {
        Toast.makeText(context, "id$orderId stautuss$status", Toast.LENGTH_SHORT).show()
        Log.d("Status", "id; $orderId status: $status")
        viewModel.changeStatus(orderId, status, isLoading = true)
    }

    private fun showLoadingStateFragment(){
        if (!progress_bar_accepted_order_screen.isVisible) {
            progBar_new_order_screen.visibility = View.VISIBLE
            activity!!.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }}

    private fun removeLoadingStateFragment(){
        if (progress_bar_accepted_order_screen.isVisible) {
            progress_bar_accepted_order_screen.visibility = View.INVISIBLE
            activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }


}