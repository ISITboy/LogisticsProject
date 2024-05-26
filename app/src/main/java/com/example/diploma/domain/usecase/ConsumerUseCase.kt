package com.example.diploma.domain.usecase

import com.example.diploma.domain.models.Consumer
import com.example.diploma.domain.repository.ConsumerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConsumerUseCase @Inject constructor(
    private val consumerRepository: ConsumerRepository
) {
    suspend fun insert(consumer: Consumer){
        consumerRepository.insert(consumer)
    }
    suspend fun delete(consumer: Consumer){
        consumerRepository.delete(consumer)
    }
    suspend fun update(consumer: Consumer){
        consumerRepository.update(consumer)
    }
    fun getAllConsumers(): Flow<List<Consumer>> = consumerRepository.getAllConsumers()
    suspend fun getConsumer(id: Int): Consumer? = consumerRepository.getConsumer(id)
}