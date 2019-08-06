package com.example.vendorapp.shared.dataclasses.retroClasses

import com.google.gson.annotations.SerializedName

data class OrdersPojo(

    var otp: Int,

    var items: List<ItemPojo>,
    //var orderId: String,

    var status: Int,

    @SerializedName("total_price")
    var totalPrice:Int

    //otp_seen:Boolean

    //var timestamp: String








)