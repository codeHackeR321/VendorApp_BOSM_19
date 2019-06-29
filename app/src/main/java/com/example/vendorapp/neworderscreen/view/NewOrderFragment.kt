package com.example.vendorapp.neworderscreen.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.neworderscreen.view.adapters.RecyclerAdapterFragment
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModel
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModelFacory
import kotlinx.android.synthetic.main.card_new_order_screen.*
import kotlinx.android.synthetic.main.fragment_fra_new_order.*

class NewOrderFragment : Fragment() , RecyclerAdapterFragment.RecyclerButtonClickListener{

    private val viewModel by lazy {
        ViewModelProviders.of(this , NewOrderViewModelFacory(this.context!!)).get(NewOrderViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fra_new_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeView()

        viewModel.error.observe(this , Observer {
            Toast.makeText(context , it , Toast.LENGTH_LONG).show()
            if (progBar_new_order_screen.isVisible && it.isNotEmpty()) {
                progBar_new_order_screen.visibility = View.INVISIBLE
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })
        super.onViewCreated(view, savedInstanceState)
    }

    fun initializeView()
    {
        Log.d("Testing NO View" , "Entered Initialize View")
        recycler_new_order_screen.adapter = RecyclerAdapterFragment(this)
        progBar_new_order_screen.visibility = View.VISIBLE
        activity!!.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        Log.d("Testing NO View" , "Refresh Data Called")
        viewModel.doInitialFetch()
        viewModel.orders.observe(this , Observer {
            Log.d("Testing NO View" , "Entered observer for orders with data = ${it.toString()}")
            (recycler_new_order_screen.adapter as RecyclerAdapterFragment).orders = it
            (recycler_new_order_screen.adapter as RecyclerAdapterFragment).notifyDataSetChanged()
            if (progBar_new_order_screen.isVisible && it.isNotEmpty()) {
                progBar_new_order_screen.visibility = View.INVISIBLE
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })
    }

    override fun buttonClicked(orderId: String, accepted: Boolean) {
        if (accepted)
        {
            Toast.makeText(context , "Accpted Order" , Toast.LENGTH_LONG).show()
        }else{
            Toast.makeText(context , "Declined Order" , Toast.LENGTH_LONG).show()
        }
    }

}
