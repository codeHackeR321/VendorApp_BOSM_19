package com.example.vendorapp.dataclasses.retroClasses

data class DayPojo(

    var day: String,

    var earnings: String,

    var orders: ArrayList<OrdersPojo>
)