package com.example.vendorapp.dataClasses.retroClasses

data class DayPojo(

    var day: String,

    var earnings: String,

    var orders: Array<OrdersPojo>
)