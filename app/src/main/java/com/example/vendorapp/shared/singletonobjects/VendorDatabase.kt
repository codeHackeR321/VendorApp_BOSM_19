package com.example.vendorapp.shared.singletonobjects

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.vendorapp.completedorderscreen.model.room.EarningDao
import com.example.vendorapp.shared.singletonobjects.model.room.OrderDao
import com.example.vendorapp.shared.dataclasses.roomClasses.ItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.MenuItemData
import com.example.vendorapp.shared.dataclasses.roomClasses.OrdersData
import com.example.vendorapp.menu.model.room.MenuDao
import com.example.vendorapp.shared.dataclasses.roomClasses.EarningData
//If someone adds a new table to database , Please dont Forget to write Migration
@Database(entities = [OrdersData::class, MenuItemData::class, ItemData::class,EarningData::class], version = 2)
abstract class VendorDatabase: RoomDatabase() {

    //companion object => same as static in java
    companion object{

        var roomInstance: VendorDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE `earning_table` (`day` TEXT NOT NULL, `earnings` TEXT NOT NULL, " +
                        "PRIMARY KEY(`day`))")
            }
        }

        // @Synchronized => only one thread can access at a time to prevent multiple instances
        @Synchronized fun getDatabaseInstance(context: Context): VendorDatabase {

            if (roomInstance == null){
                roomInstance = Room.databaseBuilder(context.applicationContext, VendorDatabase::class.java, "vendor_database")
                    .addMigrations(MIGRATION_1_2)
                    .build()
            }

            return roomInstance!!
        }
    }

    abstract fun orderDao(): OrderDao
    abstract fun menuDao(): MenuDao
    abstract fun earningDao():EarningDao

}