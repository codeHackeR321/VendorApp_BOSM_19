package com.example.vendorapp.singletonObjects

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vendorapp.acceptedOrderScreen.model.room.AcceptedOrderDao
import com.example.vendorapp.dataClasses.roomClasses.EarningsData
import com.example.vendorapp.dataClasses.roomClasses.MenuItemData
import com.example.vendorapp.dataClasses.roomClasses.OrdersData
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.newOrderScreen.model.room.NewOrderDao

@Database(entities = [OrdersData::class, EarningsData::class, MenuItemData::class], version = 1)
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