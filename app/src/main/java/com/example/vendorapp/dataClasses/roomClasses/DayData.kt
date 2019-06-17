package com.example.vendorapp.dataClasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Embedded
import org.jetbrains.annotations.NotNull

data class DayData (

    @NotNull
    @ColumnInfo(name = "day")
    var day: String,

    @NotNull
    @ColumnInfo(name = "earnings")
    var earnings: String,

    @NotNull
    @Embedded
    var orders: List<OrdersData>
)