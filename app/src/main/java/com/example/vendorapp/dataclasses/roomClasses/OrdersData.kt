package com.example.vendorapp.dataclasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "orders_table")
data class OrdersData (

    @NotNull
    @PrimaryKey
    @ColumnInfo(name = "order_id")
    var orderId: String,

    @NotNull
    @ColumnInfo(name = "status")
    var status: String,

    @NotNull
    @ColumnInfo(name = "timestamp")
    var timestamp: String,

    @NotNull
    @ColumnInfo(name = "otp")
    var otp: String,

    @NotNull
    @ColumnInfo(name = "total_amount")
    var totalAmount: String,

    @NotNull
    @Embedded
    var items: List<ItemData>

)