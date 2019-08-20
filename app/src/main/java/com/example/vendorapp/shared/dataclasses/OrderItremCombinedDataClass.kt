package com.example.vendorapp.shared.dataclasses

import java.sql.Date
import java.sql.Timestamp

data class OrderItremCombinedDataClass(

    var orderId : Int,

    var itemId: Int,

    var price : Int,

    var quantity : Int,

    var name : String,

    var status_order : Int,

    var is_loading: Boolean,

    var date: String,

    var time: String,

    var otp : Int,

    var totalAmount : Int

)