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
import com.example.vendorapp.menu.model.MenuStatus
import com.example.vendorapp.shared.dataclasses.retroClasses.MenuPojo
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import kotlinx.android.synthetic.main.menu_item.view.*
import java.lang.Exception

class MenuAdapter(private val listener: UpdateMenuListener) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    interface UpdateMenuListener {
        fun onStatusChanged(/*itemData: MenuItemData,*/ /*newTempStatus*/isNotEmpty: Boolean)
    }

    var itemList: List<MenuItemData> = emptyList()
    var newStatusItemList= mutableListOf<MenuItemData>()

    inner class MenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var itemName = view.item_name
        internal var itemPrice = view.item_price
        internal var switch = view.on_off
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuAdapter.MenuViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.menu_item, parent, false)
        Log.d("MenuCheck","enetred Oncreate View holder")
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MenuAdapter.MenuViewHolder, position: Int) {
        holder.switch.setOnCheckedChangeListener(null)

        holder.itemName.text = itemList.get(position).name
        holder.itemPrice.text = String.format(
            holder.itemView.resources.getString(R.string.new_order_total_amount),
            itemList.get(position).price
        )

        val index2=newStatusItemList.indexOfFirst { it.itemId==itemList[position].itemId }
        if (index2!=-1)
            holder.switch.isChecked=newStatusItemList[index2].status
        else
            holder.switch.isChecked=itemList[position].status


        Log.d("Menu Activity2", " actual status${itemList[position].status } position $position ")


        holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->

            when(isChecked) {
                true -> {
                    val index=newStatusItemList.indexOfFirst { it.itemId==itemList[position].itemId }
                    if (index!=-1)
                    {
                        newStatusItemList.removeAt(index)
                    }
                    else
                    {
                        val tempItemData=itemList[position]
                        tempItemData.status=true
                        newStatusItemList.add(tempItemData)
                    }
                    Log.d("Menu Activity1", " actual status${itemList[position].status } position $position Entered True")

                   /* if (newStatusItemList.isNotEmpty())*/
                        listener.onStatusChanged(/*newTempItemData,*/ newStatusItemList.isNotEmpty())


                }
                false -> {
                    val index=newStatusItemList.indexOfFirst { it.itemId==itemList[position].itemId }
                    if (index!=-1)
                    {
                        newStatusItemList.removeAt(index)
                    }
                    else
                    {
                        var tempItemData=itemList[position]
                        tempItemData.status=false
                        newStatusItemList.add(tempItemData)
                    }
                    Log.d("Menu Activity1", " actual status${itemList[position].status } position $position Entered False")
                        listener.onStatusChanged(/*newTempItemData,*/ newStatusItemList.isNotEmpty())


                    //listener.onStatusChanged(/*newTempItemData,*/ newStatusItemList.isNotEmpty())
                }
            }
        }


      /*  var index=newStatusItemList.indexOfFirst { it.itemId==itemList[position].itemId }

        if (index!=-1){
            holder.switch.isChecked=newStatusItemList[index].status
        }
        else
            holder.switch.isChecked=itemList[position].status


        holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
            var newTempItemData=itemList[position]
            when(isChecked) {
                true -> {
                    Log.d("Menu Activity1", "Entered True")
                    listener.onStatusChanged(newTempItemData, true)
                }
                false -> {
                    Log.d("Menu Activity1", "Entered False")
                    listener.onStatusChanged(newTempItemData, false)
                }
            }
        }*/
    }
}

// line 50

/*  if (itemList[position].temp_status==-1)
      {
          if (itemList[position].status==1)
          {
              holder.switch.isChecked=true
          }
          else if(itemList[position].status==0)
          {
              holder.switch.isChecked=false
          }

      }

      else
          if (itemList[position].temp_status==1)
          {
              holder.switch.isChecked=true
          }
          else if(itemList[position].temp_status==0)
          {
              holder.switch.isChecked=false
          }*/