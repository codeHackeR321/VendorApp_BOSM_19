package com.example.vendorapp.acceptedOrderScreen.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vendorapp.dataClasses.roomClasses.OrdersData


@Dao
interface AcceptedOrderDao {

    // All orders with accepted or ready status
    @Query("SELECT * FROM orders_table WHERE status = 'accepted' OR status = 'ready' ")
    fun getOrders(): LiveData<List<OrdersData>>

    @Query("DELETE FROM orders_table")
    fun deleteAll()

    @Insert
    fun insertOrder(vararg order: OrdersData)
}