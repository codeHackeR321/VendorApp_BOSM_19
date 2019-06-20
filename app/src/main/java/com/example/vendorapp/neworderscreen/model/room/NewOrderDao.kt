package com.example.vendorapp.neworderscreen.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.vendorapp.dataclasses.retroClasses.OrdersPojo
import com.example.vendorapp.dataclasses.roomClasses.OrdersData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface NewOrderDao {

    @Insert
    fun insertNewOrder(newOrder:OrdersData)

    @Query("SELECT * from orders_table WHERE status= 'Pending'")
    fun getAllNewOrders(): Flowable<List<OrdersData>>

    @Query( "UPDATE orders_table SET status = :status WHERE order_id = :orderId")
    fun updateStatus(orderId:String,status:String):Completable
}