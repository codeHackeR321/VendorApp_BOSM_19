package com.example.vendorapp.completedorderscreen.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.R
import com.example.vendorapp.shared.dataclasses.roomClasses.EarningData

class DatesAdapter(private val listener: DateSelectedListener):RecyclerView.Adapter<DatesAdapter.DatesViewHolder>() {

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
        holder.date.text=earningData.get(position).day
        holder.earning.text="\u20B9 " +earningData.get(position).earnings

        holder.parent.setOnClickListener {
            listener.OnDateSelected(earningData.get(position).day)
        }
    }

    inner class DatesViewHolder(view: View):RecyclerView.ViewHolder(view){
        internal var date= view.findViewById<TextView>(R.id.textViewDate)
        internal var earning=view.findViewById<TextView>(R.id.textViewEarning)
        internal var parent=view.findViewById<ConstraintLayout>(R.id.date_items_parent)
    }

}