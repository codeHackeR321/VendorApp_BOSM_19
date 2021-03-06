package com.example.vendorapp.neworderscreen.view

import com.example.vendorapp.shared.expandableRecyclerView.ChildDataClass

data class ModifiedOrdersDataClass (

    var orderId: Int,

    var status: Int,

    var date: String,

    var time:String,

    var otp: Int,

    var totalAmount: Int,

    var items: List<ChildDataClass>,

    var isLoading: Boolean

)