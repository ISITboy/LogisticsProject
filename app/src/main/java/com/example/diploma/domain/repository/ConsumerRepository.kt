package com.example.diploma.domain.repository

import com.example.diploma.domain.models.Consumer
import kotlinx.coroutines.flow.Flow

interface ConsumerRepository {
    suspend fun insert(consumer: Consumer)
    suspend fun delete(consumer: Consumer)
    suspend fun update(consumer: Consumer)
    fun getAllConsumers(): Flow<List<Consumer>>
    suspend fun getConsumer(id: Int): Consumer?
}