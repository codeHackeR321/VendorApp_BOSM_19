package com.example.vendorapp.neworderscreen.view

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
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
import com.example.vendorapp.shared.UIState
import com.example.vendorapp.neworderscreen.view.adapters.RecyclerAdapterFragment
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModel
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModelFacory
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.example.vendorapp.shared.utils.NetworkConnectivityCheck
import com.example.vendorapp.shared.utils.StatusKeyValue
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_fra_new_order.*

class NewOrderFragment : Fragment(), ListenerRecyViewButtonClick {

    private val viewModel by lazy {
        ViewModelProviders.of(this, NewOrderViewModelFacory(this.context!!)).get(NewOrderViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.d("LifeCyCle","New onCraeteView")
        return inflater.inflate(R.layout.fragment_fra_new_order, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeView()
        Log.d("LifeCyCle","New ViewCreated")
        super.onViewCreated(view, savedInstanceState)

    }

    fun initializeView() {
        Log.d("NewOrderFrag1", "Entered Initialize View")
        showLoadingStateFragment()
        recycler_new_order_screen.adapter = RecyclerAdapterFragment(context!!,this)

        viewModel.orders.observe(this, Observer {
            Log.d("NewOrderFrag4", "Entered observer for orders with data = ${it.toString()}")
            (recycler_new_order_screen.adapter as RecyclerAdapterFragment).orders = it.sortedByDescending { it.orderId }
            (recycler_new_order_screen.adapter as RecyclerAdapterFragment).notifyDataSetChanged()
            removeLoadingStateFragment()
        })

        viewModel.errors.observe(this, Observer {
            Log.d("NewOrderFrag3","error$it")
            Toast.makeText(activity,"Error: $it",Toast.LENGTH_LONG).show()
            removeLoadingStateFragment()
        })
        Log.d("NewOrderFrag2", "Refresh Data Called")

        observeUIState()

        viewModel.doInitialFetch()
        viewModel.getNewOrders()
    }

    override fun buttonClicked(orderId: Int, status: Int) {
        //set ui state for group view holder
        if (NetworkConnectivityCheck().checkIntenetConnection(this.context!!))
        {
            if (status==StatusKeyValue().getStatusInt(getString(R.string.status_accepted))) {

                viewModel.changeStatus(orderId, status,isLoading = true)

            } else if (status==StatusKeyValue().getStatusInt(getString(R.string.status_declined))) {
                viewModel.declineOrder(orderId,isLoading = true)

            }
            else if (status==StatusKeyValue().getStatusInt(getString(R.string.status_try_again)))
            {
                // handle by isLOading
                viewModel.fetchOrderAgain(orderId)
            }
        }
        else{
              showAlertDialogBox("Please connect to Internet or Restart the App","No Internet Connection Found",false)
        }
    }

    //loaders for grouyp view holder
    @SuppressLint("CheckResult")
    private fun observeUIState() {

        viewModel.orderUIStateNewOrder.observe(this, Observer {
            Log.d("Firestore78", "in Neworderfrag ui state$it")
            when (it!!) {
                UIState.ShowLoadingState -> {
                    //to be enabled in group view holder
                   // progBar_new_order_screen.visibility = View.VISIBLE
                    // not yet decided for group view holder
                    Log.d("Firestore76", "in Neworderfrag ui loading state")

                }

                is UIState.ErrorRoom->{
                    showAlertDialogBox("Please restart the app.","Local Database Error",true)

                }

                is UIState.SuccessStateFetchingOrders -> {
                    //removeLoadingStateFragment()

                    Toast.makeText(activity, (it as UIState.SuccessStateFetchingOrders).message, Toast.LENGTH_SHORT).show()
                    (recycler_new_order_screen.adapter as RecyclerAdapterFragment)
                        .emptyOrderIds = (it as UIState.SuccessStateFetchingOrders).incompleteOrderList
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

                is UIState.ErrorStateChangeStatus->{
                    Log.d("NewOrderFrag1","Error Change Status:${(it as UIState.ErrorStateChangeStatus).message}")
                    Toast.makeText(activity,"Error in change status ${(it as UIState.ErrorStateChangeStatus).message}}",Toast.LENGTH_LONG).show()

                }

                is UIState.SuccessStateChangeStatus->{
                    Log.d("NewOrderFrag1","Success Change Status:${(it as UIState.SuccessStateChangeStatus).message}")
                    Toast.makeText(activity,"Sccessfuly changed status",Toast.LENGTH_LONG).show()

                }
            }

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

    private fun showAlertDialogBox(message:String,title:String,isRoom:Boolean){
        val alertDialog: AlertDialog? = this.let {
            val builder = AlertDialog.Builder(context)
            builder.apply{

                setNegativeButton("ok") { dialog, id ->
                    dialog.dismiss()
                    if (isRoom){
                        activity!!.finishAffinity()
                    }
                }
            }

            builder.setMessage(message)
            builder.setTitle(title)
            builder.create()
        }
        alertDialog?.setCanceledOnTouchOutside(false)
        alertDialog?.show()
    }

    override fun onAttach(context: Context?) {
        Log.d("LifeCyCle","New on Attach")
        super.onAttach(context)
    }

    @SuppressLint("CheckResult")
    override fun onDetach() {
        Log.d("LifeCyCle","New on DeAttach")
        super.onDetach()
    }

    override fun onDestroy() {
        Log.d("LifeCyCle","New on Destroy")
        super.onDestroy()
    }

    override fun onDestroyView() {
        Log.d("LifeCyCle","New on Destroy View")
        super.onDestroyView()
    }

    override fun onPause() {
        Log.d("LifeCyCle","New on Pause")
        super.onPause()
    }

    override fun onResume() {
        Log.d("LifeCyCle","New on Resume")
        super.onResume()
    }

    override fun onStop() {
        Log.d("LifeCyCle","New on Stop")
        super.onStop()
    }

    override fun onStart() {
        Log.d("LifeCyCle","New on Start")
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("LifeCyCle","New on Crrate")
        super.onCreate(savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        Log.d("LifeCyCle","New onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }


}
