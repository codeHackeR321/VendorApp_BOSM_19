package com.example.vendorapp.neworderscreen.view

import android.annotation.SuppressLint
import android.content.Intent
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
import com.example.vendorapp.MainScreenActivity
import com.example.vendorapp.R
import com.example.vendorapp.loginscreen.view.UIState
import com.example.vendorapp.neworderscreen.view.adapters.RecyclerAdapterFragment
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModel
import com.example.vendorapp.neworderscreen.viewModel.NewOrderViewModelFacory
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.card_new_order_screen.*
import kotlinx.android.synthetic.main.fragment_fra_new_order.*

class NewOrderFragment : Fragment() , ListenerRecyViewButtonClick{

    private val viewModel by lazy {
        ViewModelProviders.of(this , NewOrderViewModelFacory(this.context!!)).get(NewOrderViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_fra_new_order, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeView()
        viewModel.observeUIState().observeOn(Schedulers.io()).subscribe({
            Log.d("Firestore78","in Neworderfrag ui state$it")
            when(it!!)
            {
                UIState.ShowLoadingState -> {
                    //to be enabled in group view holder
                    progBar_new_order_screen.visibility = View.VISIBLE
                    Log.d("Firestore76","in Neworderfrag ui loading state")

                }

                is   UIState.SuccessState->{
                    if (progBar_new_order_screen.isVisible ) {
                        progBar_new_order_screen.visibility = View.INVISIBLE
                        activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                        Log.d("Firestore56","Sucess New order frag")
                    }

                    Toast.makeText(activity,(it as UIState.SuccessState).message,Toast.LENGTH_SHORT).show()
                }
                is UIState.ErrorState ->  Toast.makeText(activity,(it as UIState.ErrorState).message,Toast.LENGTH_SHORT).show()
            }

        },{
            Log.d("Firestore77","observe observe ui statre eror$it")
        })

        viewModel.error.observe(this , Observer {
            Toast.makeText(context , it , Toast.LENGTH_LONG).show()
            if (progBar_new_order_screen.isVisible && it.isNotEmpty()) {
                progBar_new_order_screen.visibility = View.INVISIBLE
                activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            }
        })

        /*viewModel.ui_status.observe(this , Observer {
            Log.d("Firestore78","in Neworderfrag ui state$it")
            when(it!!)
            {
                UIState.ShowLoadingState -> {
                    //to be enabled in group view holder
                    progBar_new_order_screen.visibility = View.VISIBLE

                }

              is   UIState.SuccessState->{
                  if (progBar_new_order_screen.isVisible ) {
                      progBar_new_order_screen.visibility = View.INVISIBLE
                      activity!!.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
                      Log.d("Firestore56","Sucess New order frag")
                  }

                  Toast.makeText(activity,(it as UIState.SuccessState).message,Toast.LENGTH_SHORT).show()
              }
                is UIState.ErrorState ->  Toast.makeText(activity,(it as UIState.ErrorState).message,Toast.LENGTH_SHORT).show()
            }
        })
*/

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

        viewModel.getNewOrders()

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

    override fun buttonClicked(orderId: String, status: String) {
        if (status.equals(getString(R.string.status_accepted)))
        {
            Toast.makeText(context , "$status$orderId" , Toast.LENGTH_LONG).show()
        }else if(status.equals(getString(R.string.status_declined))){
            Toast.makeText(context , "$status Order$orderId" , Toast.LENGTH_LONG).show()
        }
    }

}
