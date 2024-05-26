package com.example.diploma.data.impl.repository

import com.example.diploma.domain.models.Consumer
import com.example.diploma.domain.repository.ConsumerRepository
import com.example.mapkitresultproject.data.local.room.ConsumerDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ConsumerRepositoryImpl @Inject constructor(
    private val consumerDao: ConsumerDao
) : ConsumerRepository {
    override suspend fun insert(consumer: Consumer) {
        consumerDao.insert(consumer)
    }

    override suspend fun delete(consumer: Consumer) {
        consumerDao.delete(consumer)
    }

    override suspend fun update(consumer: Consumer) {
        consumerDao.update(consumer)
    }

    override fun getAllConsumers(): Flow<List<Consumer>> = consumerDao.getAllConsumers()

    override suspend fun getConsumer(id: Int): Consumer? = consumerDao.getConsumer(id)

}