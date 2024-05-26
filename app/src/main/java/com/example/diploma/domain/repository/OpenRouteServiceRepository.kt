package com.example.diploma.domain.repository

import com.example.diploma.data.remote.dto.distances.DistanceResponse
import com.example.diploma.data.remote.dto.distances.Query
import com.example.diploma.data.remote.dto.routes.RouteResponse
import com.example.diploma.data.remote.utils.NetworkResult

interface OpenRouteServiceRepository {
    suspend fun calculateDistance(body: Query): NetworkResult<DistanceResponse>
    suspend fun getRoute(start:String, end:String):NetworkResult<RouteResponse>
}