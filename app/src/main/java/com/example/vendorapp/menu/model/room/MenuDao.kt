package com.example.vendorapp.menu.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import io.reactivex.Flowable

@Dao
interface MenuDao {

    @Query("SELECT * FROM menu_table")
    fun getMenu(): Flowable<List<MenuItemData>>

    @Query("DELETE FROM menu_table ")
    fun deleteAll()

    @Insert
    fun insertMenu(menu: List<MenuItemData>)

    @Query("SELECT * FROM menu_table WHERE item_Id = :itemId")
    fun getItemName(itemId : String) : Flowable<MenuItemData>

}