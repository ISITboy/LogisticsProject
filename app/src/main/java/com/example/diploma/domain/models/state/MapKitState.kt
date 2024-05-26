package com.example.diploma.domain.models.state

import com.example.diploma.presentation.ui.tabs.mapkit.SearchResponseItem
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Polyline

sealed interface SearchState {
    data object Off : SearchState
    data object Loading : SearchState
    data class Error(val message:String) : SearchState

    data class Success(
        val items: List<SearchResponseItem>,
        val zoomToItems: Boolean,
        val itemsBoundingBox: BoundingBox
    ) : SearchState
}

sealed interface SearchRouteState {
    data object Off : SearchRouteState
    data object Loading : SearchRouteState
    data class Error(val message:String) : SearchRouteState
    data class Success(
        val drivingRoutes: MutableList<DrivingRoute>,
        val polyline:  List<Polyline>
    ) : SearchRouteState
}