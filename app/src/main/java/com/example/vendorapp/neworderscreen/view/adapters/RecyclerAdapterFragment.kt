package com.example.vendorapp.neworderscreen.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.R
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.expandableRecyclerView.GroupDataClass
import kotlinx.android.synthetic.main.card_new_order_screen.view.*

class RecyclerAdapterFragment(val listener : RecyclerButtonClickListener) : RecyclerView.Adapter<RecyclerAdapterFragment.OrderViewHolder>(){

    interface RecyclerButtonClickListener{

        fun buttonClicked(orderId : String , accepted : Boolean)
    }

    var orders = emptyList<ModifiedOrdersDataClass>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_new_order_screen , parent , false))
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.textOrderNumber.text = String.format(holder.itemView.resources.getString(R.string.new_order_order_number) , orders[position].orderId)
        holder.textTotalAmount.text = String.format(holder.itemView.resources.getString(R.string.new_order_total_amount) , orders[position].totalAmount)
        val inflater = holder.itemView.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var list = ArrayList<GroupDataClass>()
        list.add(GroupDataClass("Menu", orders[position].items))
        holder.recyclerOrderDetails.adapter = RecyclerAdapterExpandabeRecyclerView(inflater , list)

        holder.bttnAcceptOrder.setOnClickListener {
            listener.buttonClicked(orders[position].orderId , true)
        }

        holder.bttnDeclineOrder.setOnClickListener {
            listener.buttonClicked(orders[position].orderId , false)
        }
    }

    inner class OrderViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        internal val textOrderNumber : TextView = itemView.text_card_new_order_order_id
        internal val textTotalAmount : TextView = itemView.text_card_new_order_total_amount
        internal val recyclerOrderDetails : RecyclerView = itemView.recycle_card_new_order_menu
        internal val bttnAcceptOrder : Button = itemView.bttn_card_new_order_accept
        internal val bttnDeclineOrder : Button = itemView.bttn_card_new_order_decline

    }

}