package com.example.vendorapp.neworderscreen.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.neworderscreen.view.adapters.RecyclerAdapterFragment
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModel
import kotlinx.android.synthetic.main.fragment_fra_new_order.*

class NewOrderFragment : Fragment() , RecyclerAdapterFragment.RecyclerButtonClickListener{

    private val viewModel by lazy {
        ViewModelProviders.of(viewLifecycleOwner as Fragment).get(NewOrderViewModel(context!!)::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fra_new_order, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initializeView()
    }

    fun initializeView()
    {
        recycler_new_order_screen.adapter = RecyclerAdapterFragment(this)
        viewModel.refreshOrderData()
        viewModel.orders.observe(this , Observer {
            Log.d("Testing New Order View" , "Entered observer for orders")
            (recycler_new_order_screen.adapter as RecyclerAdapterFragment).orders = it
        })
    }

    override fun buttonClicked(orderId: String, accepted: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
