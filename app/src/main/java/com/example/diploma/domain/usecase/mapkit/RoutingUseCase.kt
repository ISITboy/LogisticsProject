package com.example.diploma.domain.usecase.mapkit

import com.example.diploma.domain.repository.MapKitRoutingRepository
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import javax.inject.Inject

class RoutingUseCase @Inject constructor(private val mapKitRoutingRepository: MapKitRoutingRepository) {

    fun setDrivingOptions(drivingOptions: DrivingOptions) {
        mapKitRoutingRepository.setDrivingOptions(drivingOptions)
    }

    fun setRouteTapListener(mapObjectTapListener: MapObjectTapListener) {
        mapKitRoutingRepository.setRouteTapListener(mapObjectTapListener)
    }

    fun setVehicleOptions(vehicleOptions: VehicleOptions) {
        mapKitRoutingRepository.setVehicleOptions(vehicleOptions)
    }

    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection) {
        mapKitRoutingRepository.setMapObjectCollection(mapObjectCollection)
    }

    fun createSessionCreateRoute() {
        mapKitRoutingRepository.createSessionCreateRoute()
    }

    fun clearRoutes() {
        mapKitRoutingRepository.clearRoutes()
    }

    fun onRoutesUpdated(
        routes: List<DrivingRoute>,
        styleMainRoute: PolylineMapObject.() -> Unit,
        styleAlternativeRoute: PolylineMapObject.() -> Unit
    ) {
        mapKitRoutingRepository.onRoutesUpdated(routes,styleMainRoute, styleAlternativeRoute)
    }
    fun getPointCollection():MapObjectCollection? = mapKitRoutingRepository.getPointCollection()
    fun addRequestPoint(point: Point) {
        mapKitRoutingRepository.addRequestPoint(point)
    }

    fun getRoutingState() = mapKitRoutingRepository.getRoutingState()
    fun getPolylinesMapsObjects() = mapKitRoutingRepository.getPolylinesMapsObjects()
}