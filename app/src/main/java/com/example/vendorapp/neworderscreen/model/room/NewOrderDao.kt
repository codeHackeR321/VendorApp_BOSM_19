package com.example.vendorapp.neworderscreen.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.vendorapp.dataclasses.roomClasses.OrdersData
import io.reactivex.Observable

@Dao
interface NewOrderDao {

    @Insert
    fun insertNewOrder(vararg newOrder:OrdersData)

    @Query("SELECT * from orders_table WHERE status= 'Pending'")
    fun getAllNewOrders(): Observable<List<OrdersData>>

}