package com.example.vendorapp.dataClasses.roomClasses

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "orders_table")
data class OrdersData (

    @NotNull
    @PrimaryKey
    var orderId: String,

    @NotNull
    var status: String,

    @NotNull
    var timestamp: String,

    @NotNull
    var otp: String,

    @NotNull
    var totalAmount: String,

    @NotNull
    @Embedded
    var items: Array<ItemObject>

)