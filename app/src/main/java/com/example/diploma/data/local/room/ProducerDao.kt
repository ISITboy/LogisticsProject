package com.example.diploma.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.diploma.domain.Constants.TABLE_PRODUCER
import com.example.diploma.domain.models.Producer
import kotlinx.coroutines.flow.Flow

@Dao
interface ProducerDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(producer: Producer)
    @Update
    suspend fun update(producer: Producer)

    @Delete
    suspend fun delete(producer: Producer)

    @Query("SELECT * FROM $TABLE_PRODUCER")
    fun getAllProducers(): Flow<List<Producer>>

    @Query("SELECT * FROM $TABLE_PRODUCER WHERE id=:id")
    suspend fun getProducer(id: Int): Producer?
}