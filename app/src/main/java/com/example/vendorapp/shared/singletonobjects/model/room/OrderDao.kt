package com.example.vendorapp.shared.singletonobjects.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vendorapp.shared.dataclasses.ItemsModel
import com.example.vendorapp.shared.dataclasses.OrderItremCombinedDataClass
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


@Dao
interface OrderDao {

    @Query("SELECT * FROM orders_table WHERE status = 'Pending'")
    fun getNewOrders():Flowable<List<OrdersData>>

    @Query("SELECT * FROM orders_table WHERE status = 'accepted' OR status = 'ready' ")
    fun getOrders(): Flowable<List<OrdersData>>

    @Query("SELECT * FROM orders_table WHERE status = 'finish'")
    fun getFinishOrders(): Flowable<List<OrdersData>>

    @Query("SELECT items_order.item_id AS itemId, price, quantity, item_name AS name from items_order INNER JOIN menu_table ON items_order.item_id = menu_table.item_id WHERE order_Id= :orderId")
    fun getItemsForOrder(orderId: String): Single<List<ItemsModel>>

    @Query("SELECT  items_order.item_id AS itemId, price , quantity, items_order.order_Id AS orderId, status, timestamp, otp, item_name AS name, total_amount AS totalAmount FROM items_order JOIN orders_table JOIN menu_table ON items_order.order_Id = orders_table.order_id AND items_order.item_Id = menu_table.item_id WHERE status = 'Pending'")
    fun trialQuery() : Flowable<List<OrderItremCombinedDataClass>>

    @Query("DELETE FROM orders_table")
    fun deleteAllOrders()

    @Query("DELETE FROM items_order")
    fun deleteAllOrderItems()

    @Query("UPDATE orders_table SET status = :status WHERE order_id = :orderId")
    fun updateStatus(orderId: String, status: String): Completable

    @Insert
    fun insertOrders(orders: List<OrdersData>)

    @Insert
    fun insertOrderItems(items: List<ItemData>)

    @Query("SELECT * from orders_table WHERE order_id= :orderId ")
    fun getOrderById(orderId: String): Flowable<OrdersData>
}