package com.example.vendorapp.menu.view

import android.content.Context
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

class MenuAdapter(private val listener: UpdateMenuListener) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    interface UpdateMenuListener{
        fun onStatusChanged(itemId:String,status:String)
    }

    var itemList:List<MenuItemData> = emptyList()
    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
          internal var itemName=view.item_name
          internal var itemPrice=view.item_price
          internal var switch=view.on_off
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {
        holder.itemName.text=itemList.get(position).name
        holder.itemPrice.text=itemList.get(position).price
        if(itemList.get(position).status.equals("on"))
            holder.switch.isActivated=true
        else
            holder.switch.isActivated=false
        holder.switch.setOnClickListener {item ->
            when(item.on_off.isActivated){
                true->{ listener.onStatusChanged(itemList.get(position).itemId,"on")
                    holder.switch.isActivated=true
                }
                false -> {listener.onStatusChanged(itemList.get(position).itemId,"off")
                           holder.switch.isActivated=false
                         }
            }
        }

    }


}