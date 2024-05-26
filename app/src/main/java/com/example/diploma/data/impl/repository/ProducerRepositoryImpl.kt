package com.example.diploma.data.impl.repository

import com.example.diploma.data.local.room.ProducerDao
import com.example.diploma.domain.models.Producer
import com.example.diploma.domain.repository.ProducerRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProducerRepositoryImpl @Inject constructor(
    private val producerDao: ProducerDao
) : ProducerRepository {
    override suspend fun insert(producer: Producer) {
        producerDao.insert(producer)
    }

    override suspend fun update(producer: Producer) {
        producerDao.update(producer)
    }

    override suspend fun delete(producer: Producer) {
        producerDao.delete(producer)
    }

    override fun getAllProducer(): Flow<List<Producer>> = producerDao.getAllProducers()

    override suspend fun getProducer(id: Int): Producer? = producerDao.getProducer(id)
}