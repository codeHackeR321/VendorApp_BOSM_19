package com.example.vendorapp.acceptedOrderScreen.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "accepted_order_item")
data class AcceptedOrderItem (

    @NotNull
    @PrimaryKey
    var orderId: Int
)