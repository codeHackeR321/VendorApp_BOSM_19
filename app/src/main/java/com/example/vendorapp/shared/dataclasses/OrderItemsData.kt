package com.example.vendorapp.shared.dataclasses

import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData

data class OrderItemsData(
    var order: OrdersData,
    var items: List<ItemsModel>
)