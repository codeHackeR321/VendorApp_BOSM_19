package com.example.vendorapp.menu.view

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.OrientationEventListener
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.R
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import kotlinx.android.synthetic.main.menu_item.view.*
import java.lang.Exception

class MenuAdapter(private val listener: UpdateMenuListener) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    interface UpdateMenuListener {
        fun onStatusChanged(itemId: Int, newStatus: Int)
    }

    var itemList: List<MenuItemData> = emptyList()
    var newStatusItemList= emptyList<MenuItemData>()

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var itemName = view.item_name
        internal var itemPrice = view.item_price
        internal var switch = view.on_off
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {
        holder.itemName.text = itemList.get(position).name
        holder.itemPrice.text = String.format(
            holder.itemView.resources.getString(R.string.new_order_total_amount),
            itemList.get(position).price
        )
       if(itemList.get(position).status==0)
           holder.switch.isChecked =false
        else if(itemList.get(position).status==1)
           holder.switch.isChecked=true

        holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked) {
                true -> {
                    Log.d("Menu Activity1", "Entered True")
                    listener.onStatusChanged(itemList.get(position).itemId, 1)
                }
                false -> {
                    Log.d("Menu Activity1", "Entered False")
                    listener.onStatusChanged(itemList.get(position).itemId, 0)
                }
            }
        }
    }
}