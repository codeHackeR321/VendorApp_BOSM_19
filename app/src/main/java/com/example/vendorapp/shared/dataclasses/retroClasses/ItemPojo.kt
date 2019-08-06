package com.example.vendorapp.shared.dataclasses.retroClasses

import com.google.gson.annotations.SerializedName

data class ItemPojo(

    var quantity: Int,

    //var is_veg: boolean,
    @SerializedName("unit_price")
    var unitPrice: Int,
    // var total_price:Int,

    @SerializedName("itemclass_id")
    var itemId: Int




)





