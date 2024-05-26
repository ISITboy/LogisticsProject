package com.example.diploma.data.impl.repository


import com.example.diploma.data.remote.RemoteDataSource
import com.example.diploma.data.remote.dto.distances.DistanceResponse
import com.example.diploma.data.remote.dto.distances.Query
import com.example.diploma.data.remote.dto.routes.RouteResponse
import com.example.diploma.data.remote.utils.BaseApiResponse
import com.example.diploma.data.remote.utils.NetworkResult
import com.example.diploma.domain.repository.OpenRouteServiceRepository
import javax.inject.Inject

class OpenRouteServiceRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
): BaseApiResponse(),OpenRouteServiceRepository{

    override suspend fun calculateDistance(body: Query): NetworkResult<DistanceResponse> {
        return safeApiCall { remoteDataSource.calculateDistance(body = body)}
    }

    override suspend fun getRoute(start: String, end: String): NetworkResult<RouteResponse> {
        return safeApiCall { remoteDataSource.getRoute(start = start, end = end)}
    }

}