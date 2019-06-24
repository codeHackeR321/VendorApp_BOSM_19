package com.example.vendorapp.shared.dataclasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "earning_table")
data class EarningData (

    @PrimaryKey
    @ColumnInfo(name = "day")
    var day: String,

    @ColumnInfo(name = "earnings")
    var earnings: Long

)