package com.example.vendorapp.newOrderScreen.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "new_order_item")
data class NewOrderItem(
    @NotNull
    @PrimaryKey
    var orderId: Int
)