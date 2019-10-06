package com.example.vendorapp.acceptedorderscreen.view.ExpandableRV



import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.example.vendorapp.R
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.example.vendorapp.shared.utils.StatusKeyValue
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class GroupViewHolderAcceptedOrderScreen(private var isLoading:Boolean,private var status : Int,private val otp:Int,private val orderId: Int , private val orderAmount : Int,val listener3 :ListenerRecyViewButtonClick ,itemView : View?) :GroupViewHolder(itemView){

     var textViewOrderId : TextView
     var textViewOrderAmount:TextView
     var imageViewArrow: ImageView
     var buttonOtp:Button
     var buttonReady:Button
     var buttonFinish : Button
     var progressBar: ProgressBar


    init {
        imageViewArrow = itemView!!.findViewById(R.id.accepted_image_view_arrow_button)
        textViewOrderId=itemView.findViewById(R.id.textView_order_id)
        textViewOrderAmount=itemView.findViewById(R.id.textView_order_amount)
        buttonOtp=itemView.findViewById(R.id.button_OTP)
        buttonReady=itemView.findViewById(R.id.button_ready)
        buttonFinish=itemView.findViewById(R.id.button_finish)
        progressBar=itemView.findViewById(R.id.group_view_holder_accepted_order_screen_progressBar)

        textViewOrderId.text="#"+orderId
        textViewOrderAmount.text="\u20B9"+orderAmount

        textViewOrderAmount.setOnClickListener{}
        textViewOrderId.setOnClickListener {  }

        buttonOtp.text=otp.toString()

        if (isLoading){

            buttonOtp.visibility=View.INVISIBLE
            buttonFinish.visibility=View.INVISIBLE
            buttonReady.visibility=View.INVISIBLE
            progressBar.visibility=View.VISIBLE
        }
        else{
            if (status.equals(StatusKeyValue().getStatusInt("accepted")))
            {
                buttonOtp.visibility=View.INVISIBLE
                buttonFinish.visibility=View.INVISIBLE
                buttonReady.visibility=View.VISIBLE
            }
            else if(status.equals(StatusKeyValue().getStatusInt("ready")))
            {
                buttonOtp.visibility=View.VISIBLE
                buttonFinish.visibility=View.VISIBLE
                buttonReady.visibility=View.INVISIBLE

            }
        }



        buttonReady.setOnClickListener{

            onClickButtons(StatusKeyValue().getStatusInt("ready"))

        }

        buttonOtp.setOnClickListener{
            buttonOtp.text=otp.toString()
        }

        buttonFinish.setOnClickListener{
            onClickButtons(StatusKeyValue().getStatusInt("finish"))
            //display toast and remove the order
        }


    }

    override fun expand() {
        Log.d("Click","expand")
        imageViewArrow.setBackgroundResource( R.drawable.ic_expand_less_arrow_24px)
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


    private fun onClickButtons(status : Int)
    {

        listener3.buttonClicked(orderId,status)
    }

}
