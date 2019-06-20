package com.example.vendorapp.shared.dataclasses.retroClasses

data class EarningsPojo(

    var id: String,

    var totalEarnings: String,

    var daywise: List<DayPojo>
)


