package com.example.vendorapp.dataclasses.roomClasses

import androidx.room.*
import com.example.vendorapp.singletonobjects.TypeConverter
import org.jetbrains.annotations.NotNull

@Entity(tableName = "earnings_table")
data class EarningsData (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "total_earnings")
    var totalEarnings: String,

    @Embedded
    var daywise: List<DayData>
)
