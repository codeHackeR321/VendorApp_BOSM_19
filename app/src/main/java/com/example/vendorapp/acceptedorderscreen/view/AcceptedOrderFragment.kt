package com.example.vendorapp.acceptedorderscreen.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.acceptedorderscreen.view.adapters.AdapterForFragment
import com.example.vendorapp.acceptedorderscreen.viewModel.AcceptedOrderViewModel
import com.example.vendorapp.acceptedorderscreen.viewModel.AcceptedOrderViewModelFactory
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.neworderscreen.view.adapters.RecyclerAdapterFragment
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModel
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModelFacory
import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass
import kotlinx.android.synthetic.main.fragment_fra_accepted_order.*
import kotlinx.android.synthetic.main.fragment_fra_new_order.*


class AcceptedOrderFragment : Fragment() , AdapterForFragment.RecyclerButtonClickListener{


    override fun buttonClicked(orderId: String, status: String) {
        viewModel.changeStatus(orderId,status)
    }

     private val viewModel by lazy {
         ViewModelProviders.of(this , AcceptedOrderViewModelFactory(this.context!!)).get(AcceptedOrderViewModel::class.java)
     }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fra_accepted_order, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       initializeView()
        super.onViewCreated(view, savedInstanceState)
/*
        var ordersfake=ArrayList<ModifiedOrdersDataClass>()
        var itemsfake:List<ChildDataClass> = listOf(ChildDataClass("Sandwicvh","200","2","2323"),ChildDataClass("Sandwicvh","200","2","2323"))


        ordersfake.add(ModifiedOrdersDataClass("692","Accepted","1234567890","6776","50000",itemsfake))
       recycler_accepted_order_screen.adapter = AdapterForFragment(this)
        (recycler_accepted_order_screen.adapter as AdapterForFragment).orders=ordersfake
        (recycler_accepted_order_screen.adapter as AdapterForFragment).notifyDataSetChanged()*/
    }

     fun initializeView()
     {
         recycler_accepted_order_screen.adapter = AdapterForFragment(this)
         viewModel.getPreviousOrders()
         viewModel.acceptedOrders.observe(this , Observer {
             Log.d("Testing Accepted View" , "Entered observer for Accepted orders with list = ${it.toString()}")
             (recycler_accepted_order_screen.adapter as AdapterForFragment).orders = it
             (recycler_accepted_order_screen.adapter as AdapterForFragment).notifyDataSetChanged()
         })
     }




}