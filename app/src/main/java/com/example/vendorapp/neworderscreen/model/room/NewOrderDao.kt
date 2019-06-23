package com.example.vendorapp.neworderscreen.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vendorapp.shared.dataclasses.ItemsModel
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

    @Query("SELECT items_order.item_id AS itemId,price,quantity,item_name AS name from items_order INNER JOIN menu_table ON items_order.item_id= menu_table.item_id WHERE order_Id= :orderId")
    fun getOrderItems(orderId: String):Flowable<List<ItemsModel>>

    @Query( "UPDATE orders_table SET status = :status WHERE order_id = :orderId")
    fun updateStatus(orderId:String,status:String):Completable

    @Query("SELECT * from orders_table WHERE order_id= :orderId ")
    fun getOrderById(orderId: String): Flowable<OrdersData>

}