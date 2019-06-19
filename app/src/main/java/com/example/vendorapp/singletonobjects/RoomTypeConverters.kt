package com.example.vendorapp.singletonobjects

import androidx.room.TypeConverter
import com.example.vendorapp.dataclasses.roomClasses.DayData
import com.example.vendorapp.dataclasses.roomClasses.ItemData
import com.example.vendorapp.dataclasses.roomClasses.OrdersData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections.emptyList



class RoomTypeConverters {

    @TypeConverter
    fun dayDataToString(dayData: List<DayData>): String{

        return Gson().toJson(dayData)
    }

    @TypeConverter
    fun stringToDayData(dayData: String?): List<DayData>{

        if (dayData == null){
            return emptyList()
        }

        val dayDataType =  object : TypeToken<List<DayData>>(){}.type

        return Gson().fromJson(dayData, dayDataType)
    }

    @TypeConverter
    fun ordersDataToString(orders: List<OrdersData>): String{

        return Gson().toJson(orders)
    }

    @TypeConverter
    fun stringToOrdersData(orders: String?): List<OrdersData>{

        if (orders == null){
            return emptyList()
        }

        val ordersType = object : TypeToken<List<OrdersData>>(){}.type

        return Gson().fromJson(orders, ordersType)
    }

    @TypeConverter
    fun itemDataToString(items: List<ItemData>): String{
        return Gson().toJson(items)
    }

    @TypeConverter
    fun stringToItemData(items: String?): List<ItemData>{

        if (items == null){
            return emptyList()
        }

        val itemType = object : TypeToken<List<ItemData>>(){}.type

        return Gson().fromJson(items, itemType)

    }
}