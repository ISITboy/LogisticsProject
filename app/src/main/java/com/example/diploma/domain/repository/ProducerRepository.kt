package com.example.diploma.domain.repository

import com.example.diploma.domain.models.Producer
import kotlinx.coroutines.flow.Flow

interface ProducerRepository {

    suspend fun insert(producer: Producer)
    suspend fun update(producer: Producer)
    suspend fun delete(producer: Producer)
    fun getAllProducer(): Flow<List<Producer>>
    suspend fun getProducer(id: Int): Producer?
}