package com.example.vendorapp.menu.model.room

import androidx.room.*
import com.example.vendorapp.menu.model.MenuStatus
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface MenuDao {

    @Query("SELECT * FROM menu_table")
    fun getMenu(): Flowable<List<MenuItemData>>

    @Query("DELETE FROM menu_table ")
    fun deleteAllMenu():Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMenu(menu: List<MenuItemData>)

    @Query("SELECT * FROM menu_table WHERE item_Id = :itemId")
    fun getItemName(itemId : String) : Flowable<MenuItemData>


}