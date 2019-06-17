package com.example.vendorapp.dataClasses.roomClasses

import androidx.room.Embedded
import org.jetbrains.annotations.NotNull

data class DayData (

    @NotNull
    var day: String,

    @NotNull
    var earnings: String,

    @NotNull
    @Embedded
    var orders: Array<OrdersData>
)