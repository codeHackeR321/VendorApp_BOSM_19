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
import kotlinx.android.synthetic.main.fragment_fra_accepted_order.*


class AcceptedOrderFragment : Fragment() , AdapterForFragment.RecyclerButtonClickListener{
    override fun buttonClicked(orderId: String, status: String) {
        Log.d("Accept","ready")
        //   TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

     private val viewModel by lazy {
         // ViewModelProviders.of(viewLifecycleOwner as Fragment).get(AcceptedOrderViewModel(context!!)::class.java)
         AcceptedOrderViewModel(context!!)
     }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fra_accepted_order, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
         initializeView()
       /* var ordersfake=ArrayList<ModifiedOrdersDataClass>()
        var itemsfake:List<ChildDataClass> = listOf(ChildDataClass("Sandwicvh","200","2","2323"),ChildDataClass("Sandwicvh","200","2","2323"))


        ordersfake.add(ModifiedOrdersDataClass("692","Accepted","1234567890","6776","50000",itemsfake))
       recycler_accepted_order_screen.adapter = AdapterForFragment(this,ordersfake)
*/
    }

     fun initializeView()
     {
         recycler_accepted_order_screen.adapter = AdapterForFragment(this)
         viewModel.getPreviousOrders()
         viewModel.acceptedOrders.observe(this , Observer {
             Log.d("Testing Accepted View" , "Entered observer for orders")
             (recycler_accepted_order_screen.adapter as AdapterForFragment).orders = it
         })
     }



}