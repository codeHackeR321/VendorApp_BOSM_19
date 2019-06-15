package com.example.vendorapp.menu.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "menu_item")
data class MenuItem (

    @NotNull
    @PrimaryKey
    var ItemId: Int
)