package com.example.vendorapp.newOrderScreen.view

import com.example.vendorapp.newOrderScreen.view.expandableRecyclerView.ChildDataClass

data class ModifiedOrdersDataClass (

    var orderId: String,

    var status: String,

    var timestamp: String,

    var otp: String,

    var totalAmount: String,

    var items: List<ChildDataClass>

)