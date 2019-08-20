package com.example.vendorapp.shared.dataclasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders_table")
data class OrdersData (

    @PrimaryKey
    @ColumnInfo(name = "order_id")
    var orderId: Int,

    @ColumnInfo(name = "status_order")
    var status_order: Int,

    @ColumnInfo(name = "is_loading")
    var isLoading:Boolean,

    @ColumnInfo(name = "time")
    var time:String,

    @ColumnInfo(name = "date")
    var date:String,

    @ColumnInfo(name = "otp")
    var otp: Int,

    @ColumnInfo(name = "total_amount")
    var total_price:Int

)