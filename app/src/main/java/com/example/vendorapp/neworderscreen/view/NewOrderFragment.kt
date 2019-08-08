package com.example.vendorapp.neworderscreen.view

import android.annotation.SuppressLint
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
import androidx.lifecycle.ViewModelProviders
import com.example.vendorapp.R
import com.example.vendorapp.loginscreen.view.UIState
import com.example.vendorapp.neworderscreen.view.adapters.RecyclerAdapterFragment
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModel
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModelFacory
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.example.vendorapp.shared.utils.StatusKeyValue
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_fra_new_order.*

class NewOrderFragment : Fragment(), ListenerRecyViewButtonClick {

    private val viewModel by lazy {
        ViewModelProviders.of(this, NewOrderViewModelFacory(this.context!!)).get(NewOrderViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fra_new_order, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observeUIState()
        initializeView()
        // menu cached ordwr chaed dosplay
        viewModel.getNewOrders()

        viewModel.orders.observe(this, Observer {
            Log.d("NewOrderFrag4", "Entered observer for orders with data = ${it.toString()}")
            (recycler_new_order_screen.adapter as RecyclerAdapterFragment).orders = it
            (recycler_new_order_screen.adapter as RecyclerAdapterFragment).notifyDataSetChanged()
           removeLoadingStateFragment()
        })

        viewModel.error.observe(this, Observer {
            Log.d("NewOrderFrag3","error$it")
            Toast.makeText(activity,"Error: $it",Toast.LENGTH_LONG).show()
            removeLoadingStateFragment()
        })
        super.onViewCreated(view, savedInstanceState)
    }

    fun initializeView() {
        Log.d("NewOrderFrag1", "Entered Initialize View")
        recycler_new_order_screen.adapter = RecyclerAdapterFragment(context!!,this)
        showLoadingStateFragment()
        Log.d("NewOrderFrag2", "Refresh Data Called")
        viewModel.doInitialFetch()

    }

    override fun buttonClicked(orderId: Int, status: Int) {
        //set ui state for group view holder
        if (status==StatusKeyValue().getStatusInt(getString(R.string.status_accepted))) {

            viewModel.changeStatus(orderId, status)

        } else if (status==StatusKeyValue().getStatusInt(getString(R.string.status_declined))) {
            viewModel.declineOrder(orderId)

        }
        else if (status==StatusKeyValue().getStatusInt(getString(R.string.status_try_again)))
        {
            viewModel.fetchOrderAgain(orderId)
        }
    }

    //loaders for grouyp view holder
    @SuppressLint("CheckResult")
    private fun observeUIState() {

        viewModel.observeUIState().observeOn(AndroidSchedulers.mainThread()).subscribe({
            Log.d("Firestore78", "in Neworderfrag ui state$it")
            when (it!!) {
                UIState.ShowLoadingState -> {
                    //to be enabled in group view holder
                   // progBar_new_order_screen.visibility = View.VISIBLE
                    // not yet decided for group view holder
                    Log.d("Firestore76", "in Neworderfrag ui loading state")

                }

                is UIState.SuccessStateFetchingOrders -> {
                    //removeLoadingStateFragment()

                    Toast.makeText(activity, (it as UIState.SuccessStateFetchingOrders).message, Toast.LENGTH_SHORT).show()
                    (recycler_new_order_screen.adapter as RecyclerAdapterFragment)
                        .emptyOrderIds = (it as UIState.ErrorStateFetchingOrders).incompleteOrderList
                    (recycler_new_order_screen.adapter as RecyclerAdapterFragment).notifyDataSetChanged()
                }
                is UIState.ErrorStateFetchingOrders -> {
                   // removeLoadingStateFragment()
                    Log.d("Firestore56", "Error state fetching orders")
                    Toast.makeText(activity, (it as UIState.ErrorStateFetchingOrders).message,Toast.LENGTH_SHORT).show()
                    (recycler_new_order_screen.adapter as RecyclerAdapterFragment)
                        .emptyOrderIds = (it as UIState.ErrorStateFetchingOrders).incompleteOrderList
                    (recycler_new_order_screen.adapter as RecyclerAdapterFragment).notifyDataSetChanged()
                }
            }

        }, {
            Log.d("Firestore77", "observe observe ui statre eror$it")
        })
    }

    private fun showLoadingStateFragment(){
        if (!progBar_new_order_screen.isVisible) {
            progBar_new_order_screen.visibility = View.VISIBLE
            activity!!.window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }}

    private fun removeLoadingStateFragment(){
        if (progBar_new_order_screen.isVisible) {
            progBar_new_order_screen.visibility = View.INVISIBLE
            activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
        }
    }
}
