package com.example.vendorapp.completedorderscreen.model.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.vendorapp.shared.dataclasses.roomClasses.EarningData
import io.reactivex.Flowable

@Dao
interface EarningDao {

    @Query("SELECT * FROM earning_table")
    fun getDayWiseEarnings(): Flowable<List<EarningData>>

    @Query("DELETE FROM earning_table ")
    fun deleteAll()

    @Insert
    fun insertEarningData(earningData: List<EarningData>)

    @Query("SELECT SUM(earnings) FROM earning_table")
    fun getOverallEarnings(): Flowable<Long>



}