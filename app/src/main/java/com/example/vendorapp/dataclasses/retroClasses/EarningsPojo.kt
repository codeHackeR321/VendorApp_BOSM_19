package com.example.vendorapp.dataclasses.retroClasses

data class EarningsPojo(

    var id: String,

    var totalEarnings: String,

    var daywise: ArrayList<DayPojo>
)


