package com.example.vendorapp.dataclasses.roomClasses

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "menu_table")
data class MenuItemData (

    @PrimaryKey
    @NotNull
    var itemId: String,

    @NotNull
    var name: String,

    @NotNull
    var price: String,

    @NotNull
    var status: String
)