package com.example.diploma.domain.usecase

import com.example.diploma.domain.models.Transport
import com.example.diploma.domain.repository.TransportRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransportUseCase @Inject constructor(
    private val transportRepository: TransportRepository
) {
    suspend fun insert(transport: Transport){
        transportRepository.insert(transport)
    }
    suspend fun update(transport: Transport){
        transportRepository.update(transport)
    }
    suspend fun delete(transport: Transport){
        transportRepository.delete(transport)
    }
    fun getAllTransports(): Flow<List<Transport>> = transportRepository.getAllTransports()
    suspend fun clearColumnSelected(){
        transportRepository.clearColumnSelected()
    }
    suspend fun selectTransport(id: Int){
        transportRepository.selectTransport(id)
    }
}