package com.example.vendorapp.completedorderscreen.view.ExpandableRV

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.vendorapp.R
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class GroupViewHolderCompletedOrderScreen ( private val otp: Int, private val orderId: Int, private val orderAmount : Int, itemView : View?) :
    GroupViewHolder(itemView){

     private var textViewOrderId : TextView
     private var textViewOrderAmount:TextView
     private var imageViewArrow:ImageView
     private var textViewOtp: TextView
     private var textViewDelivered : TextView


    init {
        imageViewArrow = itemView!!.findViewById(R.id.completed_image_view_arrow_button)
        textViewOrderId=itemView.findViewById(R.id.textView_order_id)
        textViewOrderAmount=itemView.findViewById(R.id.textView_order_amount)
        textViewOtp=itemView.findViewById(R.id.textView_otp)
        textViewDelivered=itemView.findViewById(R.id.textView_delivered)

        textViewOrderId.text="Id#"+orderId
        textViewOrderAmount.text="\u20B9"+"20"+orderAmount
        textViewOtp.text=otp.toString()

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
        imageViewArrow.setBackgroundResource(R.drawable.ic_expand_less_arrow_24px)
        super.expand()
    }

    override fun collapse() {
        Log.d("Click","collapse")
        imageViewArrow.setBackgroundResource(R.drawable.ic_expand_more_arrow_24px)
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
