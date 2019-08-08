package com.example.vendorapp.neworderscreen.view.adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.vendorapp.R
import com.example.vendorapp.neworderscreen.model.IncompleteOrderStatus
import com.example.vendorapp.neworderscreen.view.ModifiedOrdersDataClass
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.example.vendorapp.shared.expandableRecyclerView.GroupDataClass
import com.example.vendorapp.shared.utils.StatusKeyValue
import kotlinx.android.synthetic.main.card_new_order_screen.view.*

class RecyclerAdapterFragment(val context: Context,val listener : ListenerRecyViewButtonClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>()/*, RecyclerAdapterExpandabeRecyclerView.RecyclerButtonClickListener2*/{

    var orders = emptyList<ModifiedOrdersDataClass>()

    var emptyOrderIds = emptyList<IncompleteOrderStatus>()


     class OrderViewHolder(itemView : View) : ViewHolder(itemView){
        internal val recyclerOrderDetails : RecyclerView = itemView.recycle_card_new_order_menu


    }

    class IncompleteOrderViewHolder(val view: View): ViewHolder(view){

        var textViewTryAgainMessage:TextView=view.findViewById(R.id.textViewTryAgainMessage)
        var textViewOrderId:TextView=view.findViewById(R.id.textViewOrderId)
        var buttonTryAgain: Button =view.findViewById(R.id.buttonTryAgain)
        var prog_bar:ProgressBar=view.findViewById(R.id.progressBarIncompleteOrder)

    }  override fun getItemViewType(position: Int): Int {
        if(position<emptyOrderIds.size)

        {
            return 0
        }
        else{

            return 1
        }

//        return super.getItemViewType(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view:View?
        //Here user no. is to be placed
        if(viewType==0)

        {

            view = LayoutInflater.from(parent.context).inflate(R.layout.card_incomplete_order_new_order_screen, parent,false) as View

            return IncompleteOrderViewHolder(view)
        }
        else{

            view = LayoutInflater.from(parent.context).inflate(R.layout.card_new_order_screen, parent,false) as View
            return OrderViewHolder(view)
        }
        /*return OrderViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_new_order_screen , parent , false))*/
    }

    override fun getItemCount(): Int {
        return orders.size +emptyOrderIds.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
       /* val inflater = holder.itemView.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var list = ArrayList<GroupDataClass>()
        list.add(GroupDataClass("Menu", orders[position].items))
        holder.recyclerOrderDetails.adapter = RecyclerAdapterExpandabeRecyclerView(orders[position].orderId
            ,orders[position].totalAmount,listener,inflater , list)
*/
        if(holder.itemViewType==0) {
            val holderIncompleteOrder: IncompleteOrderViewHolder = holder as IncompleteOrderViewHolder
            holderIncompleteOrder.textViewOrderId.text=emptyOrderIds[position].orderId.toString()
            if (emptyOrderIds[position].status.equals(context.getString(R.string.new_order_incomp_order_status_loading)))
            {
                holderIncompleteOrder.buttonTryAgain.visibility=View.INVISIBLE
                holderIncompleteOrder.textViewTryAgainMessage.visibility=View.INVISIBLE
                holderIncompleteOrder.prog_bar.visibility=View.VISIBLE
            }
            else if (emptyOrderIds[position].status.equals(context.getString(R.string.new_order_incomp_order_status_try_again)))
            {
                holderIncompleteOrder.buttonTryAgain.visibility=View.VISIBLE
                holderIncompleteOrder.textViewTryAgainMessage.visibility=View.VISIBLE
                holderIncompleteOrder.textViewTryAgainMessage.text=context.getString(R.string.new_order_incomp_order_status_try_again)
                holderIncompleteOrder.prog_bar.visibility=View.INVISIBLE
            }

            holderIncompleteOrder.buttonTryAgain.setOnClickListener(object :View.OnClickListener{
                override fun onClick(v: View?) {
                    emptyOrderIds[position].status=context.getString(R.string.new_order_incomp_order_status_loading)
                    notifyDataSetChanged()
                    listener.buttonClicked(emptyOrderIds[position].orderId.toInt(),StatusKeyValue().getStatusInt("try again"))

                }
            })

        }

        else
        {
            val holderOrderViewHolder:OrderViewHolder=holder as OrderViewHolder
            val inflater = holder.itemView.context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            var list = ArrayList<GroupDataClass>()
            list.add(GroupDataClass("Menu", orders[position].items))
            holderOrderViewHolder.recyclerOrderDetails.adapter = RecyclerAdapterExpandabeRecyclerView(orders[position].orderId
                ,orders[position].totalAmount,listener,inflater , list)
        }
    }







}