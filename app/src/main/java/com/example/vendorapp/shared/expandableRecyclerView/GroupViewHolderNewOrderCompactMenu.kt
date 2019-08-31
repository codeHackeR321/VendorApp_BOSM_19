package com.example.vendorapp.shared.expandableRecyclerView

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.res.TypedArrayUtils
import com.example.vendorapp.R
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.example.vendorapp.shared.utils.StatusKeyValue
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class GroupViewHolderNewOrderCompactMenu(
    private var isLoading: Boolean,
    private val orderId: Int,
    private val orderAmount: Int,
    val listener3: ListenerRecyViewButtonClick,
    itemView: View?
) : GroupViewHolder(itemView) {

    var textViewOrderId: TextView
    var textViewOrderAmount: TextView
    var imageViewArrow:ImageView
    var buttonAccept: Button
    var buttonDecline: Button
    var progressBar: ProgressBar


    init {
        //initialising views
        imageViewArrow = itemView!!.findViewById(R.id.image_view_arrow_button)
        textViewOrderId = itemView!!.findViewById(R.id.textView_order_id)
        textViewOrderAmount = itemView.findViewById(R.id.textView_order_amount)
        buttonAccept = itemView.findViewById(R.id.button_accept)
        buttonDecline = itemView.findViewById(R.id.button_decline)
        progressBar = itemView.findViewById(R.id.text_group_view_holder_new_order_progress_bar)

        //setting initial states of views
        textViewOrderId.text = "#$orderId"
        textViewOrderAmount.text = "\u20B9" + orderAmount

        textViewOrderAmount.setOnClickListener {}
        textViewOrderId.setOnClickListener { }

        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            buttonDecline.visibility = View.INVISIBLE
            buttonAccept.visibility = View.INVISIBLE
        } else {
            progressBar.visibility = View.INVISIBLE
            buttonDecline.visibility = View.VISIBLE
            buttonAccept.visibility = View.VISIBLE

        }

        buttonAccept.setOnClickListener {

            onClickButtons(StatusKeyValue().getStatusInt("accepted"))
        }

        buttonDecline.setOnClickListener {
            onClickButtons(StatusKeyValue().getStatusInt("declined"))
        }
    }

    override fun expand() {
        Log.d("Click", "expand")
        imageViewArrow.setBackgroundResource(R.drawable.ic_expand_less_arrow_24px)
        super.expand()
    }

    override fun collapse() {
        Log.d("Click", "collapse")
        imageViewArrow.setBackgroundResource(R.drawable.ic_expand_more_arrow_24px)
        super.collapse()
    }

    private fun onClickButtons(status: Int) {

        listener3.buttonClicked(orderId, status)
    }

}