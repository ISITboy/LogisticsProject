package com.example.diploma.data.impl.repository

import android.content.Context
import android.util.Log
import com.example.diploma.domain.models.DrivingOptionsBuilder
import com.example.diploma.domain.models.state.SearchRouteState
import com.example.diploma.domain.models.VehicleOptionsBuilder
import com.example.diploma.domain.repository.MapKitRoutingRepository
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouterType
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class MapKitRoutingRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : MapKitRoutingRepository {

    private val drivingRouter =
        DirectionsFactory.getInstance().createDrivingRouter(DrivingRouterType.COMBINED)
    private var drivingOptions = DrivingOptionsBuilder().build()
    private var vehicleOptions = VehicleOptionsBuilder().build()
    private var session: DrivingSession? = null
    private val state = MutableStateFlow<SearchRouteState>(SearchRouteState.Off)

    private var requestedPoints: MutableList<RequestPoint> = mutableListOf()
    private var routingCollection: MapObjectCollection? = null
    private var pointCollection: MapObjectCollection? = null
    private val polylinesMapsObjects: MutableList<PolylineMapObject?> = mutableListOf()
    private var routingTapListener: MapObjectTapListener? = null

    private val drivingRouteListener = object : DrivingSession.DrivingRouteListener {
        override fun onDrivingRoutes(drivingRoutes: MutableList<DrivingRoute>) {
            val polyline = drivingRoutes.map { it.geometry }
            Log.d("MyLog", "distance first ${drivingRoutes.first().metadata.weight.distance.text}")
            Log.d("MyLog", "distance last ${drivingRoutes.last().metadata.weight.distance.text}")
            Log.d("MyLog", "size d ${drivingRoutes.size}")
            state.value = SearchRouteState.Success(drivingRoutes, polyline)
        }

        override fun onDrivingRoutesError(error: Error) {
            when (error) {
                is NetworkError -> state.value =
                    SearchRouteState.Error("Search request error due network issues")

                else -> state.value =
                    SearchRouteState.Error("Search request unknown error")

            }
        }
    }

    override fun setDrivingOptions(drivingOptions: DrivingOptions) {
        this.drivingOptions = drivingOptions
    }

    override fun setVehicleOptions(vehicleOptions: VehicleOptions) {
        this.vehicleOptions = vehicleOptions
    }

    override fun setRouteTapListener(mapObjectTapListener: MapObjectTapListener) {
        routingTapListener = mapObjectTapListener
    }

    override fun createSessionCreateRoute() {
        session = drivingRouter.requestRoutes(
            requestedPoints.takeLast(2),
            drivingOptions,
            vehicleOptions,
            drivingRouteListener
        )
        state.value = SearchRouteState.Loading
    }

    override fun getPointCollection():MapObjectCollection? = pointCollection

    override fun clearRoutes() {
        polylinesMapsObjects.forEach { polylineMapObject ->
            if (polylineMapObject?.isValid == true) routingTapListener?.let {
                polylineMapObject.removeTapListener(it)
            }
        }
        polylinesMapsObjects.clear()
        routingCollection?.clear()
        pointCollection?.clear()
        requestedPoints.clear()
        Log.d("MyLog", "polylinesMapsObjects.size ${polylinesMapsObjects.size}")
        Log.d("MyLog", "requestedPoints.size ${requestedPoints.size}")

    }

    override fun getRoutingState(): MutableStateFlow<SearchRouteState> = state

    override fun setMapObjectCollection(mapObjectCollection: MapObjectCollection) {
        routingCollection = mapObjectCollection.addCollection()
        pointCollection = mapObjectCollection.addCollection()
    }

    override fun onRoutesUpdated(
        routes: List<DrivingRoute>,
        styleMainRoute: PolylineMapObject.() -> Unit,
        styleAlternativeRoute: PolylineMapObject.() -> Unit
    ) {
        routes.forEachIndexed { index, route ->
            this.polylinesMapsObjects.add(routingCollection?.addPolyline(route.geometry)?.apply {
                routingTapListener?.let { addTapListener(it) }
                userData = route
                if (index == 0) styleMainRoute() else styleAlternativeRoute()
            })
        }
    }

    override fun getPolylinesMapsObjects(): MutableList<PolylineMapObject?> = polylinesMapsObjects

    override fun addRequestPoint(point: Point) {
        this.requestedPoints.add(RequestPoint(point, RequestPointType.WAYPOINT, null, null))
        if (this.requestedPoints.size > 1) createSessionCreateRoute()
    }

}