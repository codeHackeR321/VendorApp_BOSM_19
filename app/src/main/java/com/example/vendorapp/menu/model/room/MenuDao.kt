package com.example.vendorapp.menu.model.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vendorapp.dataClasses.roomClasses.MenuItemData

@Dao
interface MenuDao {

    @Query("SELECT * FROM menu_table")
    fun getMenu(): LiveData<List<MenuItemData>>

    @Query("DELETE FROM menu_table ")
    fun deleteAll()

    @Insert
    fun insertMenu(vararg menuItemData: MenuItemData)

    @Query("SELECT * FROM menu_table WHERE itemId > :itemId")
    fun getItemName(itemId : String) : MenuItemData

}