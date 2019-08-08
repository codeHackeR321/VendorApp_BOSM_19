package com.example.vendorapp.shared.utils

class StatusKeyValue {
    var map = mapOf<String, Int>(Pair("pending", 0)
        , Pair("accepted", 1), Pair("ready", 2), Pair("finish", 3), Pair("declined", 4),Pair("try again",5) )

    fun getStatusInt(status : String):Int{
        return map[status]!!
    }
}