package com.example.vendorapp.shared.dataclasses

import java.sql.Timestamp

data class OrderItremCombinedDataClass(

    var orderId : Int,

    var itemId: Int,

    var price : Int,

    var quantity : Int,

    var name : String,

    var status : Int,

    var timestamp : Long,

    var otp : Int,

    var totalAmount : Int

)