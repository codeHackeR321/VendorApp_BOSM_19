package com.example.vendorapp.shared.expandableRecyclerView

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.res.TypedArrayUtils
import com.example.vendorapp.R
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class GroupViewHolderNewOrderCompactMenu(private val orderId: String , private val orderAmount : String,val listener3 :ListenerRecyViewButtonClick ,itemView : View?) :GroupViewHolder(itemView){

    lateinit var textViewOrderId : TextView
    lateinit var textViewOrderAmount:TextView
    lateinit var textArrowButton:TextView
    lateinit var buttonAccept:Button
    lateinit var buttonDecline:Button


    init {
        textArrowButton = itemView!!.findViewById(R.id.text_group_view_holder_new_order_heading)
        textViewOrderId=itemView!!.findViewById(R.id.textView_order_id)
        textViewOrderAmount=itemView.findViewById(R.id.textView_order_amount)
        buttonAccept=itemView.findViewById(R.id.button_accept)
        buttonDecline=itemView.findViewById(R.id.button_decline)

        textViewOrderId.text="Id#5666"+orderId
        textViewOrderAmount.text="\u20B9"+"20"+orderAmount
        textViewOrderAmount.setOnClickListener{}
        textViewOrderId.setOnClickListener {  }

        buttonAccept.setOnClickListener{

            onClickButtons("accepted")
        }

        buttonDecline.setOnClickListener{
            onClickButtons("declined")
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