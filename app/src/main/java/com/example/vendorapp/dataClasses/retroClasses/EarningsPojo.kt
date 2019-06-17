package com.example.vendorapp.dataClasses.retroClasses

data class EarningsPojo(

    var id: String,

    var totalEarnings: String,

    var daywise: Array<DayPojo>
)


