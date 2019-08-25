package com.example.vendorapp.shared.dataclasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu_table")
data class MenuItemData (

    @PrimaryKey
    @ColumnInfo(name = "item_id")
    var itemId: Int,

    @ColumnInfo(name = "item_name")
    var name: String,

    @ColumnInfo(name = "item_price")
    var price: Int,

    @ColumnInfo(name = "item_status")
    var status: Int
)