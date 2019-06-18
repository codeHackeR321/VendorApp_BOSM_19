package com.example.vendorapp.dataclasses.roomClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "menu_table")
data class MenuItemData (

    @PrimaryKey
    var itemId: String,

    var name: String,

    var price: String,

    var status: String
)