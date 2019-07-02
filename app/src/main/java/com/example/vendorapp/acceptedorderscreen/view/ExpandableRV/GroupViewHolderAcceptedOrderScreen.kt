package com.example.vendorapp.acceptedorderscreen.view.ExpandableRV



import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.vendorapp.R
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class GroupViewHolderAcceptedOrderScreen(private var status : String,private val otp: String,private val orderId: String , private val orderAmount : String,val listener3 :ListenerRecyViewButtonClick ,itemView : View?) :GroupViewHolder(itemView){

    lateinit var textViewOrderId : TextView
    lateinit var textViewOrderAmount:TextView
    lateinit var textArrowButton:TextView
    lateinit var buttonOtp:Button
    lateinit var buttonReady:Button
    lateinit var buttonFinish : Button
/*
    interface RecyclerButtonClickListener3{

        fun buttonClicked3(orderId : String , accepted : Boolean)
    }*/

    init {
        textArrowButton = itemView!!.findViewById(R.id.text_group_view_holder_new_order_heading)
        textViewOrderId=itemView!!.findViewById(R.id.textView_order_id)
        textViewOrderAmount=itemView.findViewById(R.id.textView_order_amount)
        buttonOtp=itemView.findViewById(R.id.button_OTP)
        buttonReady=itemView.findViewById(R.id.button_ready)
        buttonFinish=itemView.findViewById(R.id.button_finish)

        textViewOrderId.text="Id#"+orderId
        textViewOrderAmount.text="\u20B9"+"20"+orderAmount

        textViewOrderAmount.setOnClickListener{}
        textViewOrderId.setOnClickListener {  }

        buttonReady.setOnClickListener{

            onClickButtons("ready")
            buttonFinish.visibility=View.VISIBLE
            buttonOtp.visibility=View.VISIBLE
            buttonReady.visibility=View.GONE
        }

        buttonOtp.setOnClickListener{
            buttonOtp.text=otp
        }

        buttonFinish.setOnClickListener{
            onClickButtons("finish")
            //display toast and remove the order
        }

        if (status.equals("accepted"))
        {
            buttonOtp.visibility=View.INVISIBLE
            buttonFinish.visibility=View.INVISIBLE
            buttonReady.visibility=View.VISIBLE
        }
        else if(status.equals("ready"))
        {
            buttonOtp.visibility=View.VISIBLE
            buttonFinish.visibility=View.VISIBLE
            buttonReady.visibility=View.INVISIBLE

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

    fun setHeading(){
        // textHeading.text = "Menu"
        // super.onClick(textHeading)
        // super.expand()
    }

    private fun onClickButtons(status : String)
    {

        listener3.buttonClicked(orderId,status)
    }

}