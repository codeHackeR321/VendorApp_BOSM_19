package com.example.vendorapp.shared.expandableRecyclerView

import android.view.View
import android.widget.TextView
import com.example.vendorapp.R
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder

class GroupViewHolderNewOrderCompactMenu(itemView : View?) :GroupViewHolder(itemView){

    lateinit var textHeading : TextView

    init {
        textHeading = itemView!!.findViewById(R.id.text_group_view_holder_new_order_heading)
    }

    override fun expand() {
        textHeading.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_arrow, 0)
        super.expand()
    }

    override fun collapse() {
        textHeading.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_arrow, 0)
        super.collapse()
    }

    fun setHeading(){
        textHeading.text = "Menu"
    }

}