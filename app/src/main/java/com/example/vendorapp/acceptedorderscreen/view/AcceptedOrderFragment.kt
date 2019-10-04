package com.example.vendorapp.acceptedorderscreen.view

import android.annotation.SuppressLint
import android.app.AlertDialog
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
import com.example.vendorapp.neworderscreen.view.adapters.RecyclerAdapterFragment
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.example.vendorapp.shared.UIState
import com.example.vendorapp.shared.utils.NetworkConnectivityCheck
import io.reactivex.android.schedulers.AndroidSchedulers
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
            (recycler_accepted_order_screen.adapter as AdapterForFragment).orders = it.sortedByDescending { it.orderId }
            (recycler_accepted_order_screen.adapter as AdapterForFragment).notifyDataSetChanged()
        })

        viewModel.error.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            removeLoadingStateFragment()
        })
        observeUIState()
        viewModel.getAcceptedOrders()

    }


    override fun buttonClicked(orderId: Int, status: Int) {
        Toast.makeText(context, "id$orderId stautuss$status", Toast.LENGTH_SHORT).show()
        Log.d("Status", "id; $orderId status: $status")
       if (NetworkConnectivityCheck().checkIntenetConnection(this.context!!)){
           viewModel.changeStatus(orderId, status, isLoading = true)
       }
        else
       {
           showAlertDialogBox("Please connect to Internet or Restart the App","No Internet Connection Found",false)
       }
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
                is UIState.ErrorRoom->{
                    showAlertDialogBox("Please restart the app.","Local Database Error",true)
                }

                is UIState.ErrorStateChangeStatus->{
                    Log.d("NewOrderFrag1","Error Change Status:${(it as UIState.ErrorStateChangeStatus).message}")
                    Toast.makeText(activity,"Error changing status ${(it as UIState.ErrorStateChangeStatus).message}}", Toast.LENGTH_LONG).show()

                }

                is UIState.SuccessStateChangeStatus->{
                    Log.d("NewOrderFrag1","Success Change Status:${(it as UIState.SuccessStateChangeStatus).message}")
                    Toast.makeText(activity,"Status change successful",Toast.LENGTH_LONG).show()

                }
            }

        }, {
            Log.d("Firestore89", "observe observe ui state  Acceptederror$it")
            Toast.makeText(activity,"Error observing UI State Accepted Order frag$it",Toast.LENGTH_LONG).show()
        })
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
}