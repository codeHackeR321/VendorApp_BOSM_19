package com.example.vendorapp.shared.singletonobjects.model.room

import androidx.room.*
import com.example.vendorapp.shared.dataclasses.ItemsModel
import com.example.vendorapp.shared.dataclasses.OrderItremCombinedDataClass
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.POST


@Dao
interface OrderDao {

    @Query("SELECT * FROM orders_table WHERE status_order = 0")
    fun getNewOrders():Flowable<List<OrdersData>>


    @Query("SELECT * FROM orders_table WHERE status_order = 1 OR status_order = 2 ")
    fun getOrders(): Flowable<List<OrdersData>>

    @Query("SELECT * FROM orders_table WHERE status_order = 3")
    fun getFinishOrders(): Flowable<List<OrdersData>>

    @Query("SELECT items_order.item_id AS itemId, price, quantity, item_name AS name from items_order INNER JOIN menu_table ON items_order.item_id = menu_table.item_id WHERE order_Id= :orderId")
    fun getItemsForOrder(orderId: String): Single<List<ItemsModel>>

    @Query("SELECT  items_order.item_id AS itemId, price , quantity, items_order.order_Id AS orderId, status_order, is_loading,timestamp, otp, item_name AS name, total_amount AS totalAmount FROM items_order JOIN orders_table JOIN menu_table ON items_order.order_Id = orders_table.order_id AND items_order.item_Id = menu_table.item_id WHERE status_order = 0")
    fun getAllNewOrders() : Flowable<List<OrderItremCombinedDataClass>>

    @Query("SELECT  items_order.item_id AS itemId, price , quantity, items_order.order_Id AS orderId, status_order, is_loading,timestamp, otp, item_name AS name, total_amount AS totalAmount FROM items_order JOIN orders_table JOIN menu_table ON items_order.order_Id = orders_table.order_id AND items_order.item_Id = menu_table.item_id WHERE status_order = 1 OR status_order = 2")
    fun getAllAcceptedOrders() : Flowable<List<OrderItremCombinedDataClass>>

    @Query("SELECT  items_order.item_id AS itemId, price , quantity, items_order.order_Id AS orderId, status_order, is_loading,timestamp, otp, item_name AS name, total_amount AS totalAmount FROM items_order JOIN orders_table JOIN menu_table ON items_order.order_Id = orders_table.order_id AND items_order.item_Id = menu_table.item_id WHERE status_order = 3")
    fun getAllFinishedOrdersRoom() : Flowable<List<OrderItremCombinedDataClass>>

    @Query("UPDATE orders_table SET status_order = :status,is_loading= :isLoading WHERE order_id = :orderId")
    fun updateOrderStatusRoom(orderId: Int, status: Int, isLoading: Boolean): Completable

    @Query("UPDATE orders_table SET is_loading = :isLoading WHERE order_id = :orderId")
    fun updateLoadingStatusRoom(orderId: Int, isLoading: Boolean): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrders(orders: OrdersData)

    @Insert
    fun insertOrderItems(items: List<ItemData>)

    @Query("DELETE FROM items_order")
    fun deleteAllOrderItems()

    @Query("DELETE FROM items_order WHERE order_Id=:orderId    ")
    fun deleteItemsWithOrderId(orderId: Int)

    @Query("SELECT * from orders_table WHERE order_id= :orderId ")
    fun getOrderById(orderId: Int): Flowable<OrdersData>

    @Query("SELECT order_id from orders_table")
    fun getAllOrdersRoom(): Single<List<Int>>
}