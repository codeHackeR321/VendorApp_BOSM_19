package com.example.vendorapp.completedOrderScreen.view.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.R
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.neworderscreen.view.adapters.RecyclerAdapterExpandabeRecyclerView
import com.example.vendorapp.shared.expandableRecyclerView.GroupDataClass
import kotlinx.android.synthetic.main.card_new_order_screen.view.*

class OrdersAdapterFragment:RecyclerView.Adapter<OrdersAdapterFragment.OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
       val view= LayoutInflater.from(parent.context).inflate(R.layout.card_completed_order_screen,parent,false)
       return OrderViewHolder(view)
    }
   var orders:List<ModifiedOrdersDataClass> = emptyList()
    override fun getItemCount(): Int {
          return orders.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.textOrderNumber.text= String.format(R.string.new_order_order_number.toString(),orders.get(position).orderId)
        holder.textTotalAmount.text= String.format(R.string.new_order_total_amount.toString(),orders.get(position).totalAmount)
        val inflater = holder.itemView.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var list = ArrayList<GroupDataClass>()
        list.add(GroupDataClass("Menu", orders[position].items))
        holder.recyclerOrderDetails.adapter = RecyclerAdapterExpandabeRecyclerView(inflater , list)
    }

    inner class OrderViewHolder(view: View):RecyclerView.ViewHolder(view){
        internal val textOrderNumber : TextView = view.text_card_new_order_order_id
        internal val textTotalAmount : TextView = view.text_card_new_order_total_amount
        internal val recyclerOrderDetails : RecyclerView = view.recycle_card_new_order_menu
    }
}