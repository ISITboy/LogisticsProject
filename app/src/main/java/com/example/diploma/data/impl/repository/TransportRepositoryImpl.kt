package com.example.diploma.data.impl.repository

import com.example.diploma.data.local.room.TransportDao
import com.example.diploma.domain.models.Transport
import com.example.diploma.domain.repository.TransportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransportRepositoryImpl @Inject constructor(
    private val transportDao: TransportDao
) : TransportRepository {
    override suspend fun insert(transport: Transport) {
        transportDao.insert(transport)
    }

    override suspend fun update(transport: Transport) {
        transportDao.update(transport)
    }

    override suspend fun delete(transport: Transport) {
        transportDao.delete(transport)
    }

    override fun getAllTransports(): Flow<List<Transport>> = transportDao.getAllTransport()
    override suspend fun clearColumnSelected() {
        transportDao.clearColumnSelected()
    }

    override suspend fun selectTransport(id: Int) {
        transportDao.selectTransport(id)
    }
}