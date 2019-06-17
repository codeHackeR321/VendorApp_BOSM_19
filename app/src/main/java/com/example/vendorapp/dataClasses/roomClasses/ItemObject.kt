package com.example.vendorapp.dataClasses.roomClasses

import androidx.room.ColumnInfo
import org.jetbrains.annotations.NotNull


data class ItemObject (

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