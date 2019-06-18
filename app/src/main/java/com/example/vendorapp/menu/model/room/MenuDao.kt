package com.example.vendorapp.menu.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vendorapp.dataclasses.roomClasses.MenuItemData
import io.reactivex.Observable

@Dao
interface MenuDao {

    @Query("SELECT * FROM menu_table")
    fun getMenu(): Observable<List<MenuItemData>>

    @Query("DELETE FROM menu_table ")
    fun deleteAll()

    @Insert
    fun insertMenu(vararg menuItemData: MenuItemData)

    @Query("SELECT * FROM menu_table WHERE itemId > :itemId")
    fun getItemName(itemId : String) : MenuItemData

}