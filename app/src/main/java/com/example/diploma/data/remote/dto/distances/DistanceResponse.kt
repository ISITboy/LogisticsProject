package com.example.diploma.data.remote.dto.distances

data class DistanceResponse(
    val destinations: List<Destination>,
    val distances: List<List<Double>>,
    val metadata: Metadata,
    val sources: List<Source>
)