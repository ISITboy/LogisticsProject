package com.example.diploma.data.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.diploma.domain.Constants.TABLE_TRANSPORT
import com.example.diploma.domain.models.Transport
import kotlinx.coroutines.flow.Flow

@Dao
interface TransportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transport: Transport)

    @Update
    suspend fun update(transport: Transport)

    @Delete
    suspend fun delete(transport: Transport)

    @Query("SELECT * FROM $TABLE_TRANSPORT")
    fun getAllTransport(): Flow<List<Transport>>

    @Query("UPDATE transports SET selected = 0")
    suspend fun clearColumnSelected()

    @Query("UPDATE transports SET selected = 1 WHERE id = :id")
    suspend fun selectTransport(id: Int)

}