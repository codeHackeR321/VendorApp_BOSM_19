package com.example.vendorapp.dataClasses.roomClasses

import org.jetbrains.annotations.NotNull


data class ItemObject (

    @NotNull
    var itemId: String,

    @NotNull
    var price: String,

    @NotNull
    var quantity: String
)