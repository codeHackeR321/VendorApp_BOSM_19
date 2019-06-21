package com.example.vendorapp.neworderscreen.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import retrofit2.http.DELETE

@Dao
interface NewOrderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewOrder(newOrder:OrdersData)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderItems(items:List<ItemData>)

    @Query("SELECT * from orders_table WHERE status= 'Pending'")
    fun getAllNewOrders(): Flowable<List<OrdersData>>

    @Query("SELECT * from items_order WHERE order_Id= :orderId")
    fun getOrderItems(orderId: String):Flowable<List<ItemData>>

    @Query( "UPDATE orders_table SET status = :status WHERE order_id = :orderId")
    fun updateStatus(orderId:String,status:String):Completable

    @Query("DELETE FROM orders_table")
    fun deleteAllOrders()

    @Query("DELETE FROM items_order")
    fun deleteAllOrderItems()

    @Query("SELECT * from orders_table WHERE order_id= :orderId ")
    fun getOrderById(orderId: String): Flowable<OrdersData>

    @Query("SELECT item_name from menu_table WHERE item_id= :itemId ")
    fun getItemName(itemId:String): Single<String>
}