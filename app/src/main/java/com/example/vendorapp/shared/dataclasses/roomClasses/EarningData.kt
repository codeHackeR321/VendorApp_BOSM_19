package com.example.vendorapp.shared.dataclasses.roomClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "earning_table")
data class EarningData (

    @PrimaryKey
    @ColumnInfo(name = "date")
    var date: String,

    @ColumnInfo(name = "earnings")
    var earnings:Int

)