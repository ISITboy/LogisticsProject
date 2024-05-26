package com.example.diploma.domain.repository

import com.example.diploma.domain.models.state.SearchState
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchOptions
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Queue

interface MapKitSearchRepository {
    fun getSearchState(): MutableStateFlow<SearchState>
    fun setVisibleRegion(region: VisibleRegion?)
    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection)
    fun getPointCollection():MapObjectCollection?
    fun createSession(query: String,searchOptions:SearchOptions)
    fun createSession(point: Point, searchOptions:SearchOptions)

    fun createSession(query: Queue<String>, searchOptions: SearchOptions)
    fun subscribeForSearch(): Flow<*>
}