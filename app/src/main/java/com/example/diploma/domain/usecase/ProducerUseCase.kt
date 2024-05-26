package com.example.diploma.domain.usecase

import com.example.diploma.domain.models.Producer
import com.example.diploma.domain.repository.ProducerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProducerUseCase @Inject constructor(
    private val producerRepository: ProducerRepository
) {
    suspend fun insert(producer: Producer){
        producerRepository.insert(producer)
    }
    suspend fun update(producer: Producer){
        producerRepository.update(producer)
    }
    suspend fun delete(producer: Producer){
        producerRepository.delete(producer)
    }
    fun getAllProducer(): Flow<List<Producer>> = producerRepository.getAllProducer()
    suspend fun getProducer(id: Int): Producer? = producerRepository.getProducer(id)
}