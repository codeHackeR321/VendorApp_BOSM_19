package com.example.vendorapp.dataClasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

@Entity(tableName = "earnings_table")
data class EarningsData (

    @NotNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @NotNull
    @ColumnInfo(name = "total_earnings")
    var totalEarnings: String,

    @NotNull
    @Embedded
    var daywise: List<DayData>
)