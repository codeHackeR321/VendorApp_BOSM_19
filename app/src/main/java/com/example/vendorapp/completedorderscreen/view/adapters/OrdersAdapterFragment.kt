package com.example.vendorapp.completedorderscreen.view.adapters

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.R
import com.example.vendorapp.completedorderscreen.view.ExpandableRV.ExpandableRVAdapter
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.neworderscreen.view.adapters.RecyclerAdapterExpandabeRecyclerView
import com.example.vendorapp.shared.expandableRecyclerView.GroupDataClass
import kotlinx.android.synthetic.main.card_accepted_order_screen.view.*
import kotlinx.android.synthetic.main.card_completed_order_screen.view.*
import kotlinx.android.synthetic.main.card_new_order_screen.view.*

class OrdersAdapterFragment:RecyclerView.Adapter<OrdersAdapterFragment.OrderViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        Log.d("checkr","created")
       val view= LayoutInflater.from(parent.context).inflate(R.layout.card_accepted_order_screen,parent,false)
       return OrderViewHolder(view)
    }
   var orders:List<ModifiedOrdersDataClass> = emptyList()
    override fun getItemCount(): Int {
          return orders.size

    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        Log.d("checkr",orders.get(position).orderId+orders.get(position).totalAmount)

        val inflater = holder.itemView.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var list = ArrayList<GroupDataClass>()
        list.add(GroupDataClass("Menu", orders[position].items))

        holder.recyclerOrderDetails.adapter = ExpandableRVAdapter(orders[position].otp,orders[position].orderId,orders[position].totalAmount,inflater , list)
    }

    inner class OrderViewHolder(view: View):RecyclerView.ViewHolder(view){
        internal val recyclerOrderDetails : RecyclerView = itemView.recycle_card_accepted_order_menu
    }
}