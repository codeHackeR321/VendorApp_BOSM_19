package com.example.vendorapp.shared.dataclasses.retroClasses

data class OrdersPojo(

    var orderId: String,

    var status: String,

    var timestamp: String,

    var otp: String,

    var totalAmount: String,

    var items: List<ItemPojo>
)