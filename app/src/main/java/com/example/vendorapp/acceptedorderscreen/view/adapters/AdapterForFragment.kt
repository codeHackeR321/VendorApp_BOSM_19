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
import com.example.vendorapp.acceptedorderscreen.view.ExpandableRV.ExpandableRVAdapter
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.example.vendorapp.shared.expandableRecyclerView.GroupDataClass
import kotlinx.android.synthetic.main.card_accepted_order_screen.view.*


class AdapterForFragment(val listener : ListenerRecyViewButtonClick  ) : RecyclerView.Adapter<AdapterForFragment.OrderViewHolder>(){

    var orders = emptyList<ModifiedOrdersDataClass>()





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_accepted_order_screen , parent , false))
    }

    override fun getItemCount(): Int {
        return orders.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {


        val inflater = holder.itemView.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var list = ArrayList<GroupDataClass>()
        list.add(GroupDataClass("Menu", orders[position].items))
        holder.recyclerOrderDetails.adapter = ExpandableRVAdapter(orders[position].isLoading,orders[position].status,orders[position].otp,orders[position].orderId,orders[position].totalAmount,listener,inflater , list)


    }

    inner class OrderViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        internal val recyclerOrderDetails : RecyclerView = itemView.recycle_card_accepted_order_menu


    }

}