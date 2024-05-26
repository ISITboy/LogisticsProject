package com.example.diploma.domain.usecase.mapkit

import com.example.diploma.domain.repository.MapKitSearchRepository
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchOptions
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Queue
import javax.inject.Inject

class SearchUseCase @Inject constructor(
    private val searchRepository: MapKitSearchRepository
) {

    fun getSearchState() = searchRepository.getSearchState()
    fun setVisibleRegion(region: VisibleRegion?){
        searchRepository.setVisibleRegion(region)
    }
    fun getSearchPointCollection(): MapObjectCollection? = searchRepository.getPointCollection()

    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection){
        searchRepository.setMapObjectCollection(mapObjectCollection)
    }
    fun createSession(query: String,searchOptions: SearchOptions){
        searchRepository.createSession(query,searchOptions)
    }
    fun createSession(point: Point, searchOptions: SearchOptions){
        searchRepository.createSession(point,searchOptions)
    }
    fun createSession(query: Queue<String>, searchOptions: SearchOptions){
        searchRepository.createSession(query,searchOptions)
    }
    fun subscribeForSearch(): Flow<*> = searchRepository.subscribeForSearch()

}