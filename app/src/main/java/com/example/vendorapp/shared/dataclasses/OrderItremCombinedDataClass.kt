package com.example.vendorapp.shared.dataclasses

import java.sql.Timestamp

data class OrderItremCombinedDataClass(

    var orderId : String,

    var itemId: String,

    var price : String,

    var quantity : String,

    var name : String,

    var status : String,

    var timestamp : Long,

    var otp : String,

    var totalAmount : String

)