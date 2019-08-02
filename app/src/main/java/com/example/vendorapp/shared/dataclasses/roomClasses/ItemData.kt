package com.example.vendorapp.shared.dataclasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items_order")
data class ItemData (

    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @ColumnInfo(name = "item_id")
    var itemId: Int,

    @ColumnInfo(name = "order_Id")
    var orderId: Int,

    @ColumnInfo(name = "price")
    var price: Int,

    @ColumnInfo(name = "quantity")
    var quantity: Int
)