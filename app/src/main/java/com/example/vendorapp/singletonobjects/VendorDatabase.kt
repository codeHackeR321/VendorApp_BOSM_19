package com.example.vendorapp.singletonobjects

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vendorapp.acceptedorderscreen.model.room.AcceptedOrderDao
import com.example.vendorapp.dataclasses.roomClasses.ItemData
import com.example.vendorapp.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.neworderscreen.model.room.NewOrderDao

@Database(entities = [OrdersData::class, MenuItemData::class, ItemData::class], version = 1)
abstract class VendorDatabase: RoomDatabase() {

    //companion object => same as static in java
    companion object{

        var roomInstance: VendorDatabase? = null

        // @Synchronized => only one thread can access at a time to prevent multiple instances
        @Synchronized fun getDatabaseInstance(context: Context): VendorDatabase {

            if (roomInstance == null){
                roomInstance = Room.databaseBuilder(context.applicationContext, VendorDatabase::class.java, "vendor_database"  )
                    .build()
            }

            return roomInstance!!
        }
    }

    abstract fun newOrderDao(): NewOrderDao
    abstract fun acceptedOrderDao(): AcceptedOrderDao
    abstract fun menuDao(): MenuDao

}