package com.example.diploma.domain.repository

import com.example.diploma.domain.models.Transport
import kotlinx.coroutines.flow.Flow

interface TransportRepository {
    suspend fun insert(transport: Transport)
    suspend fun update(transport:Transport)
    suspend fun delete(transport:Transport)
    fun getAllTransports(): Flow<List<Transport>>
    suspend fun clearColumnSelected()
    suspend fun selectTransport(id: Int)
}