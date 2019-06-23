package com.example.vendorapp.shared.singletonobjects

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vendorapp.shared.singletonobjects.model.room.OrderDao
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.menu.model.room.MenuDao

@Database(entities = [OrdersData::class, MenuItemData::class, ItemData::class], version = 1)
abstract class VendorDatabase: RoomDatabase() {

    //companion object => same as static in java
    companion object{

        var roomInstance: VendorDatabase? = null

        // @Synchronized => only one thread can access at a time to prevent multiple instances
        @Synchronized fun getDatabaseInstance(context: Context): VendorDatabase {

            if (roomInstance == null){
                roomInstance = Room.databaseBuilder(context.applicationContext, VendorDatabase::class.java, "vendor_database")
                    .build()
            }

            return roomInstance!!
        }
    }

    abstract fun orderDao(): OrderDao
    abstract fun menuDao(): MenuDao

}