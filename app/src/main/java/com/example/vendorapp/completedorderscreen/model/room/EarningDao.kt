package com.example.vendorapp.completedorderscreen.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vendorapp.shared.dataclasses.roomClasses.EarningData
import com.google.android.material.circularreveal.CircularRevealHelper
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface EarningDao {

    @Query("SELECT * FROM earning_table")
    fun getDayWiseEarnings(): Flowable<List<EarningData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEarningData(earningData: List<EarningData>)

}