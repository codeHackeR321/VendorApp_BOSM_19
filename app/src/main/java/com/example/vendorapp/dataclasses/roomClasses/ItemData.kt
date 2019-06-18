package com.example.vendorapp.dataclasses.roomClasses

import androidx.room.ColumnInfo
import org.jetbrains.annotations.NotNull


data class ItemData (

    @ColumnInfo(name = "item_id")
    var itemId: String,

    @ColumnInfo(name = "price")
    var price: String,

    @ColumnInfo(name = "quantity")
    var quantity: String
)