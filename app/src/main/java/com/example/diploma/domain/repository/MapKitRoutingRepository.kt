package com.example.diploma.domain.repository

import com.example.diploma.domain.models.state.SearchRouteState
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import kotlinx.coroutines.flow.MutableStateFlow

interface MapKitRoutingRepository {
    fun setDrivingOptions(drivingOptions: DrivingOptions)
    fun setRouteTapListener(mapObjectTapListener: MapObjectTapListener)
    fun setVehicleOptions(vehicleOptions: VehicleOptions)
    fun createSessionCreateRoute()
    fun clearRoutes()
    fun getRoutingState(): MutableStateFlow<SearchRouteState>
    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection)
    fun onRoutesUpdated(
        routes: List<DrivingRoute>,
        styleMainRoute: PolylineMapObject.() -> Unit,
        styleAlternativeRoute: PolylineMapObject.() -> Unit
    )
    fun getPolylinesMapsObjects(): MutableList<PolylineMapObject?>
    fun getPointCollection():MapObjectCollection?
    fun addRequestPoint(point: Point)
}