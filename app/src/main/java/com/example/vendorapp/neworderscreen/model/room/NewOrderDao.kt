package com.example.vendorapp.neworderscreen.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface NewOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewOrder(newOrder:OrdersData)

    @Query("SELECT * from orders_table WHERE status= 'Pending'")
    fun getAllNewOrders(): Flowable<List<OrdersData>>

    @Query( "UPDATE orders_table SET status = :status WHERE order_id = :orderId")
    fun updateStatus(orderId:String,status:String):Completable
}