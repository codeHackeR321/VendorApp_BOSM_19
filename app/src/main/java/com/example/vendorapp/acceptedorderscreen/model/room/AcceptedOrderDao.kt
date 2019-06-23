package com.example.vendorapp.acceptedorderscreen.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vendorapp.shared.dataclasses.ItemsModel
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import io.reactivex.Completable
import io.reactivex.Flowable


@Dao
interface AcceptedOrderDao {

    @Query("SELECT * FROM orders_table WHERE status = 'accepted' OR status = 'ready' ")
    fun getOrders(): Flowable<List<OrdersData>>

    @Query("SELECT items_order.item_id AS itemId,price,quantity,item_name AS name from items_order INNER JOIN menu_table ON items_order.item_id= menu_table.item_id WHERE order_Id= :orderId")
    fun getItemsForOrder(orderId: String): Flowable<List<ItemsModel>>

    @Query("DELETE FROM orders_table")
    fun deleteAllOrders()

    @Query("DELETE FROM items_order")
    fun deleteAllOrderItems()

    @Query("UPDATE orders_table SET status = :status WHERE order_id = :orderId")
    fun updateStatus(orderId: String, status: String): Completable

    @Insert
    fun insertOrder(orders: List<OrdersData>)

    @Insert
    fun insertOrderItems(items: List<ItemData>)
}