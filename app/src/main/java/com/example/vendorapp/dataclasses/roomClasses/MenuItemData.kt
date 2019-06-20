package com.example.vendorapp.dataclasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "menu_table")
data class MenuItemData (

    @PrimaryKey
    @ColumnInfo(name = "item_id")
    var itemId: String,

    @ColumnInfo(name = "item_name")
    var name: String,

    @ColumnInfo(name = "item_price")
    var price: String,

    @ColumnInfo(name = "item_status")
    var status: String
)