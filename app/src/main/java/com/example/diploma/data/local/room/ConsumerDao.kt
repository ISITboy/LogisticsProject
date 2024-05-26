package com.example.mapkitresultproject.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.diploma.domain.Constants.TABLE_CONSUMER
import com.example.diploma.domain.models.Consumer
import kotlinx.coroutines.flow.Flow

@Dao
interface ConsumerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(consumer: Consumer)

    @Update
    suspend fun update(consumer: Consumer)
    @Delete
    suspend fun delete(consumer: Consumer)
    @Query("SELECT * FROM $TABLE_CONSUMER")
    fun getAllConsumers(): Flow<List<Consumer>>
    @Query("SELECT * FROM $TABLE_CONSUMER WHERE id=:id")
    suspend fun getConsumer(id: Int): Consumer?
}