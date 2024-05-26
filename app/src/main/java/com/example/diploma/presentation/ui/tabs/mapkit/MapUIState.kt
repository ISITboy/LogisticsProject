package com.example.diploma.presentation.ui.tabs.mapkit

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point



data class SearchResponseItem(
    val point: Point,
    val geoObject: GeoObject?,
)

sealed interface ButtonEvent{
    data object RemoveRouts :ButtonEvent
    data object RemoveObjects :ButtonEvent
    data object SelectSubscribeForSearch :ButtonEvent
}
