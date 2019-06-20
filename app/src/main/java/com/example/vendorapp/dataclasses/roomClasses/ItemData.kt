package com.example.vendorapp.dataclasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "items_per_order")
data class ItemData (

    @PrimaryKey
    @ColumnInfo(name = "item_id")
    var itemId: String,

    @ColumnInfo(name = "order_Id")
    var orderId: String,

    @ColumnInfo(name = "price")
    var price: String,

    @ColumnInfo(name = "quantity")
    var quantity: String
)