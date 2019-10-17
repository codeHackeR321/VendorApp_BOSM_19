package com.example.vendorapp.menu.model.room

import androidx.room.*
import com.example.vendorapp.menu.model.MenuStatus
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface MenuDao {

    @Query("SELECT * FROM menu_table")
    fun getMenu(): Flowable<List<MenuItemData>>

    @Query("DELETE FROM menu_table ")
    fun deleteAllMenu():Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMenu(menu: List<MenuItemData>)

    @Query("SELECT * FROM menu_table WHERE item_Id = :itemId")
    fun getItemName(itemId : Int) : Flowable<MenuItemData>

    @Query("SELECT * FROM menu_table WHERE item_status_temp >= 0")
    fun getMenuListToBeUpdated(): Single<List<MenuItemData>>

    @Query("Update menu_table set item_status_temp = :tempStatus WHERE  item_id=:itemId")
    fun setNewTempStatus(itemId: Int, tempStatus:Int): Completable

    @Query("SELECT COUNT(item_status_temp) FROM menu_table WHERE item_status_temp >=0")
    fun getSaveChangesSelectedStatus(): Flowable<Int>

}