package com.example.vendorapp.completedorderscreen.view.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.R
import com.example.vendorapp.shared.dataclasses.roomClasses.EarningData

class DatesAdapter(private var lastSelectedDate:String, private val listener: DateSelectedListener):RecyclerView.Adapter<DatesAdapter.DatesViewHolder>() {

    private var currentSelectedDate=lastSelectedDate
    interface DateSelectedListener{

       fun OnDateSelected(date:String)
    }


    var earningData:List<EarningData> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatesViewHolder {
        val view:View= LayoutInflater.from(parent.context).inflate(R.layout.date_items,parent,false)
        return DatesViewHolder(view)
    }

    override fun getItemCount(): Int {
   return earningData.size
    }

    override fun onBindViewHolder(holder: DatesViewHolder, position: Int) {
        holder.date.text=earningData.get(position).date
        holder.earning.text="\u20B9 "  +earningData.get(position).earnings
        Log.d("DateCheck2","last$lastSelectedDate c $currentSelectedDate")
        if (holder.date.text.equals(lastSelectedDate))
        {
            holder.parent.setBackgroundResource(R.drawable.shape_rectangle_white_bg)
        }
        if (holder.date.text.equals(currentSelectedDate))
            holder.parent.setBackgroundResource(R.drawable.shape_rectangle_grey_bg)


        holder.parent.setOnClickListener {
            lastSelectedDate=currentSelectedDate
            currentSelectedDate=earningData.get(position).date
            Log.d("Date Check","last$lastSelectedDate cuurr $currentSelectedDate")
            listener.OnDateSelected(earningData.get(position).date)
        }
    }

    inner class DatesViewHolder(view: View):RecyclerView.ViewHolder(view){
        internal var date= view.findViewById<TextView>(R.id.textViewDate)
        internal var earning=view.findViewById<TextView>(R.id.textViewEarning)
        internal var parent=view.findViewById<ConstraintLayout>(R.id.date_items_parent)
    }

}