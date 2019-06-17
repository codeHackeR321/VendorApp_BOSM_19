package com.example.vendorapp.newOrderScreen.view.expandableRecyclerView

import android.view.View
import android.widget.TextView
import com.example.vendorapp.R
import com.example.vendorapp.dataClasses.retroClasses.ItemPojo
import com.example.vendorapp.singletonObjects.VendorDatabase
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder
import kotlinx.android.synthetic.main.child_view_holder_new_order_order_detail.view.*

class ChildViewHolderNewOrderOrderDetails(itemView : View?) : ChildViewHolder(itemView){

    fun onBind(item : ChildDataClass , group : ExpandableGroup<*>){

        var totalCost = Integer.toString(Integer.parseInt(item.quantity) * Integer.parseInt(item.price))
        var itemName = VendorDatabase.getDatabaseInstance(itemView.context).menuDao().getItemName(item.itemId!!).name
        itemView.findViewById<TextView>(R.id.text_child_view_holder_new_order_item_name).text = itemName
        itemView.findViewById<TextView>(R.id.text_child_view_holder_new_order_itemwise_price).text = String.format(itemView.resources.getString(R.string.new_order_item_wise_price) , item.price)
        itemView.findViewById<TextView>(R.id.text_child_view_holder_new_order_item_quantity).text = item.quantity
        itemView.findViewById<TextView>(R.id.text_child_view_holder_new_order_item_cost).text = String.format(itemView.resources.getString(R.string.new_order_total_amount) , totalCost)

    }

}