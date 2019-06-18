package com.example.vendorapp.dataclasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Embedded
import org.jetbrains.annotations.NotNull

data class DayData (

    @ColumnInfo(name = "day")
    var day: String,

    @ColumnInfo(name = "earnings")
    var earnings: String,

    @Embedded
    var orders: ArrayList<OrdersData>
)