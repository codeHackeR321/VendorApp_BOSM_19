package com.example.vendorapp.dataclasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "orders_table")
data class OrdersData (

    @PrimaryKey
    @ColumnInfo(name = "order_id")
    var orderId: String,

    @ColumnInfo(name = "status")
    var status: String,

    @ColumnInfo(name = "timestamp")
    var timestamp: String,

    @ColumnInfo(name = "otp")
    var otp: String,

    @ColumnInfo(name = "total_amount")
    var totalAmount: String,

    @Embedded
    var items: List<ItemData>

)