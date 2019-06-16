package com.example.vendorapp.singletonObjects

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vendorapp.acceptedOrderScreen.model.room.AcceptedOrderDao
import com.example.vendorapp.acceptedOrderScreen.model.room.AcceptedOrderItem
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.newOrderScreen.model.room.NewOrderDao
import com.example.vendorapp.newOrderScreen.model.room.NewOrderItem

@Database(entities = [NewOrderItem::class, AcceptedOrderItem::class], version = 1)
abstract class VendorDatabase: RoomDatabase() {

    //companion object => same as static in java
    companion object{

        private var roomInstance: VendorDatabase? = null

        // @Synchronized => only one thread can access to prevent multiple instances
        @Synchronized fun getInstance(context: Context): VendorDatabase {

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