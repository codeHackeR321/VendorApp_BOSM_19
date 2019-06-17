package com.example.vendorapp.dataClasses.roomClasses

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vendorapp.dataClasses.roomClasses.DayData
import org.jetbrains.annotations.NotNull

@Entity(tableName = "earnings_table")
data class EarningsData (

    @NotNull
    @PrimaryKey(autoGenerate = true)
    var id: Int,

    @NotNull
    var totalEarnings: String,

    @NotNull
    @Embedded
    var daywise: Array<DayData>
)