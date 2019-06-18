package com.example.vendorapp.dataclasses.roomClasses

import androidx.room.ColumnInfo
import org.jetbrains.annotations.NotNull


data class ItemData (

    @NotNull
    @ColumnInfo(name = "item_id")
    var itemId: String,

    @NotNull
    @ColumnInfo(name = "price")
    var price: String,

    @NotNull
    @ColumnInfo(name = "quantity")
    var quantity: String
)