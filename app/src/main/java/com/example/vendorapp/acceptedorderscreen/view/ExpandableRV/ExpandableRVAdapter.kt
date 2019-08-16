package com.example.vendorapp.acceptedorderscreen.view.ExpandableRV


import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.vendorapp.R
import com.example.vendorapp.shared.Listeners.ListenerRecyViewButtonClick
import com.example.vendorapp.shared.expandableRecyclerView.ChildViewHolderNewOrderOrderDetails
import com.example.vendorapp.shared.expandableRecyclerView.GroupDataClass
import com.example.vendorapp.shared.expandableRecyclerView.GroupViewHolderNewOrderCompactMenu
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class ExpandableRVAdapter(private var isLoading:Boolean,private val status:Int ,  private val otp:Int,private val orderId: Int,private val orderAmount: Int
                                            , val listener2 : ListenerRecyViewButtonClick ,var inflater : LayoutInflater, var list: List<ExpandableGroup<*>>) :

    ExpandableRecyclerViewAdapter<GroupViewHolderAcceptedOrderScreen, ChildViewHolderNewOrderOrderDetails>(list){


    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): GroupViewHolderAcceptedOrderScreen {
        return(GroupViewHolderAcceptedOrderScreen(isLoading,status,otp,orderId , orderAmount,listener2,
            inflater.inflate(
                R.layout.group_view_holder_accepted_order_screen,
                parent,
                false
            )
        ))
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): ChildViewHolderNewOrderOrderDetails {
        return(ChildViewHolderNewOrderOrderDetails(
            inflater.inflate(
                R.layout.child_view_holder_new_order_order_detail,
                parent,
                false
            )
        ))
    }

    override fun onBindChildViewHolder(holder: ChildViewHolderNewOrderOrderDetails?, flatPosition: Int, group: ExpandableGroup<*>?, childIndex: Int) {
        val order = (group as GroupDataClass).items[childIndex]
        holder!!.onBind(order , group)
    }

    override fun onBindGroupViewHolder(holder: GroupViewHolderAcceptedOrderScreen?, flatPosition: Int, group: ExpandableGroup<*>?) {
        //holder!!.setHeading()
    }

}