package com.example.vendorapp.acceptedorderscreen.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vendorapp.dataclasses.roomClasses.OrdersData
import io.reactivex.Observable


@Dao
interface AcceptedOrderDao {

    // All orders with accepted or ready status
    @Query("SELECT * FROM orders_table WHERE status = 'accepted' OR status = 'ready' ")
    fun getOrders(): Observable<List<OrdersData>>

    @Query("DELETE FROM orders_table")
    fun deleteAll()

    @Insert
    fun insertOrder(vararg order: OrdersData)
}