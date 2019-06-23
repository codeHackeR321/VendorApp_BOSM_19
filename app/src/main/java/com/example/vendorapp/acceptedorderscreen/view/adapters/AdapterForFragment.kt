package com.example.vendorapp.acceptedorderscreen.view.adapters

import com.example.vendorapp.neworderscreen.view.adapters.RecyclerAdapterExpandabeRecyclerView



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
import kotlinx.android.synthetic.main.card_accepted_order_screen.view.*


class AdapterForFragment(val listener : RecyclerButtonClickListener, private var orders: ArrayList<ModifiedOrdersDataClass>  ) : RecyclerView.Adapter<AdapterForFragment.OrderViewHolder>(){

    interface RecyclerButtonClickListener{

        fun buttonClicked(orderId : String , status : String) // Ready or Finish
    }

    //  var orders = emptyList<ModifiedOrdersDataClass>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_accepted_order_screen , parent , false))
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

        holder.bttnReadyOrder.setOnClickListener {
            listener.buttonClicked(orders[position].orderId , "Ready")
        }

        /*holder.bttnDeclineOrder.setOnClickListener {
            listener.buttonClicked(orders[position].orderId , false)
        }*/
    }

    inner class OrderViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        internal val textOrderNumber : TextView = itemView.text_card_accepted_order_order_id
        internal val textTotalAmount : TextView = itemView.text_card_accepted_order_total_amount
        internal val recyclerOrderDetails : RecyclerView = itemView.recycle_card_accepted_order_menu
        internal val bttnReadyOrder : Button = itemView.bttn_card_accepted_order_ready


    }

}