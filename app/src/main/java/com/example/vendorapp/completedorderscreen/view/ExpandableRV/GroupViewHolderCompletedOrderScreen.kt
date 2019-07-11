package com.example.vendorapp.completedorderscreen.view.ExpandableRV

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.vendorapp.R
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class GroupViewHolderCompletedOrderScreen ( private val otp: String, private val orderId: String, private val orderAmount : String, itemView : View?) :
    GroupViewHolder(itemView){

    lateinit var textViewOrderId : TextView
    lateinit var textViewOrderAmount:TextView
    lateinit var textArrowButton:TextView
    lateinit var textViewOtp: TextView
    lateinit var textViewDelivered : TextView


    init {
        textArrowButton = itemView!!.findViewById(R.id.text_group_view_holder_new_order_heading)
        textViewOrderId=itemView.findViewById(R.id.textView_order_id)
        textViewOrderAmount=itemView.findViewById(R.id.textView_order_amount)
        textViewOtp=itemView.findViewById(R.id.textView_otp)
        textViewDelivered=itemView.findViewById(R.id.textView_delivered)

        textViewOrderId.text="Id#"+orderId
        textViewOrderAmount.text="\u20B9"+"20"+orderAmount
        textViewOtp.text=otp

        textViewOrderAmount.setOnClickListener{}
        textViewOrderId.setOnClickListener {  }



        textViewOtp.setOnClickListener{

        }

        textViewDelivered.setOnClickListener{
            //display toast and remove the order
        }


    }

    override fun expand() {
        Log.d("Click","expand")
        textArrowButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0)
        super.expand()
    }

    override fun collapse() {
        Log.d("Click","collapse")
        textArrowButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0)
        super.collapse()
    }


    override fun setOnGroupClickListener(listener: OnGroupClickListener?) {
        Log.d("Click","onGroup")

        super.setOnGroupClickListener(listener)

    }

    override fun onClick(v: View?) {
        Log.d("Click","onClick")

        super.onClick(v)

    }





}
