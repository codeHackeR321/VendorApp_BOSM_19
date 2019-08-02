package com.example.vendorapp.shared.dataclasses.retroClasses

data class OrdersPojo(

    //var orderId: String,

    var status: Int,

    //var timestamp: String,

    //var otp: String,



    var items: List<ItemPojo>,

    var total_price:Int
)