package com.example.vendorapp.completedOrderScreen.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vendorapp.R

class DatesAdapter(private val listener:DateSelectedListener):RecyclerView.Adapter<DatesAdapter.DatesViewHolder>() {

    interface DateSelectedListener{

       fun OnDateSelected(date:String)
    }

    var dates:List<String> = emptyList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatesViewHolder {
        val view:View= LayoutInflater.from(parent.context).inflate(R.layout.date_items,parent,false)
        return DatesViewHolder(view)
    }

    override fun getItemCount(): Int {
   return dates.size
    }

    override fun onBindViewHolder(holder: DatesViewHolder, position: Int) {
        holder.date.text=dates.get(position)
        holder.date.setOnClickListener {
            listener.OnDateSelected(dates.get(position))
        }
    }

    inner class DatesViewHolder(view: View):RecyclerView.ViewHolder(view){
        internal var date= view.findViewById<TextView>(R.id.date)
    }

}