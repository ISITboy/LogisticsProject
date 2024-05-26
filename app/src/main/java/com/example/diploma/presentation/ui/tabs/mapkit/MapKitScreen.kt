package com.example.diploma.presentation.ui.tabs.mapkit

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.diploma.R
import com.example.diploma.domain.models.CameraPositionBuilder
import com.example.diploma.domain.models.state.SearchRouteState
import com.example.diploma.domain.models.state.SearchState
import com.example.diploma.domain.models.VehicleOptionsBuilder
import com.example.diploma.presentation.mainActivity.MainViewModel
import com.example.diploma.presentation.ui.tabs.ProgressIndicator
import com.example.diploma.presentation.ui.tabs.mapkit.detailsSheet.BottomSheetState
import com.example.diploma.presentation.ui.tabs.mapkit.detailsSheet.DetailsSheetRouting
import com.example.diploma.presentation.ui.tabs.mapkit.detailsSheet.DetailsSheetSearch
import com.example.diploma.presentation.ui.tabs.mapkit.detailsSheet.DetailsSheetViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.directions.driving.VehicleType
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.runtime.image.ImageProvider
import kotlinx.coroutines.delay

@Composable
fun MapKitScreen(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel,
    mapViewModel: MapKitViewModel,
    detailsVewModel: DetailsSheetViewModel,
    pointsForCreateRouts:String
) {
    val context = LocalContext.current
    val searchState = mapViewModel.getSearchState().collectAsState().value
    val createRouteState = mapViewModel.getRoutingState().collectAsState().value
    val keySubscribeForSearch = mapViewModel.keySubscribeForSearch.collectAsState().value
    val showProgressIndicator = mapViewModel.statusProgressIndicator.collectAsState()
    val showSheet = mapViewModel.showSheet.collectAsState().value
    val searchedObject by mapViewModel.searchedObject.collectAsState()
    val invalid by mapViewModel.invalid.collectAsState()
    val activeSubscribeForSearch by mapViewModel.activeSubscribeForSearch.collectAsState()



    //TODO: посмотри на key - можно ли поменять на activeSubscribeForSearch
    LaunchedEffect(key1 = keySubscribeForSearch, block = {
        Log.d(
            "MyLog",
            "mapViewModel.searchedObject.value.isNotEmpty() ${mapViewModel.searchedObject.value.isNotEmpty()}"
        )
        Log.d("MyLog", "activeSubscribeForSearch $activeSubscribeForSearch")
        if (mapViewModel.searchedObject.value.isNotEmpty() && activeSubscribeForSearch) {
            Log.d("MyLog", "LaunchedEffect1")
            mapViewModel.subscribeForSearch()
        }
    })

    LaunchedEffect(key1 = pointsForCreateRouts, block = {
        Log.d("micha","points $pointsForCreateRouts")
        if(pointsForCreateRouts!="empty"){
            pointsForCreateRouts.split(",").chunked(2).forEach {
                delay(2000)
                mapViewModel.createSession(Point(it.first().toDouble(),it.last().toDouble()))
                mapViewModel.addRequestPoint(Point(it.first().toDouble(),it.last().toDouble()))
            }
        }
    })

    val cameraListener = CameraListener { _, _, _, finished ->
        Log.d("MyLog", "camera is moving")
        if (finished) {
            Log.d("MyLog", "camera is stopping")
            mapViewModel.changeKeySubscribeForSearch()
            mapViewModel.setVisibleRegion(mainViewModel.mapView?.map?.visibleRegion)
        }
    }

    val routingInputListener = object : InputListener {
        override fun onMapTap(map: Map, point: Point) {
            Log.d("MyLog", "on mapTap")
            mapViewModel.searchUseCase.getSearchPointCollection()?.addPlacemark()?.apply {
                val bulletIcon =
                    ImageProvider.fromBitmap(mapViewModel.createBitmapFromVector(R.drawable.ic_selected_point_24))
                geometry = point
                setIcon(bulletIcon)
            }
        }

        override fun onMapLongTap(map: Map, point: Point) {
            Log.d("MyLog", "on longTap")
            mapViewModel.addRequestPoint(point)
            mapViewModel.routingUseCase.getPointCollection()?.addPlacemark()?.apply {
                val bulletIcon =
                    ImageProvider.fromBitmap(mapViewModel.createBitmapFromVector(R.drawable.ic_circle_24))
                geometry = point
                setIcon(bulletIcon)
            }
        }
    }

    val searchResultPlacemarkTapListener = MapObjectTapListener { mapObject, _ ->
        val selectedObject = (mapObject.userData as? GeoObject)
        mapViewModel.changeStateSheet(BottomSheetState.ShowBottomSheetSearchState(selectedObject))
        true
    }



    LaunchedEffect(Unit) {
        mainViewModel.initializeMapView(context)
        mainViewModel.mapView?.map?.move(
            CameraPositionBuilder().setZoom(15f).build(),
            Animation(Animation.Type.SMOOTH, 2f), null
        )
        mapViewModel.map = mainViewModel.mapView?.map
        mainViewModel.mapView?.map?.addInputListener(routingInputListener)
//        mainViewModel.mapView?.map?.addInputListener(mapViewModel.routingInputListener)
        mainViewModel.mapView?.map?.addCameraListener(cameraListener)
//        mapViewModel.map?.addCameraListener(mapViewModel.cameraListener)
        mapViewModel.setVisibleRegion(mainViewModel.mapView?.map?.visibleRegion)
        mapViewModel.setDrivingOptions()
        mapViewModel.setRouteTapListener(mapViewModel.routingTapListener)
        mapViewModel.setVehicleOptions(
            VehicleOptionsBuilder().setVehicleType(VehicleType.TRUCK).build()
        )
        mainViewModel.mapView?.map?.mapObjects?.let {
            mapViewModel.setMapObjectCollection(it)
        }
    }

    MapContainerComposable(
        mapView = mainViewModel.mapView,
        searchedObject = searchedObject,
        invalid = invalid,
        showProgressIndicator = showProgressIndicator,
        mapViewModel = mapViewModel,
        activeSubscribeForSearch = activeSubscribeForSearch,
        onEventButton = mapViewModel::containerButtonEvent
    ) {
        LaunchedEffect(searchState) {
            when (searchState) {
                is SearchState.Error -> {
                    mapViewModel.changeStatusProgressIndicator(ProgressIndicator.HIDE)
                }

                SearchState.Off -> {}
                is SearchState.Success -> {
                    mapViewModel.changeStatusProgressIndicator(ProgressIndicator.HIDE)
                    val successSearchState = searchState as? SearchState.Success
                    val searchItems = successSearchState?.items ?: emptyList()
                    if(pointsForCreateRouts=="empty"){
                        mapViewModel.updateSearchResponsePlacemarks(
                            searchItems,
                            searchResultPlacemarkTapListener
                        )
                    }else{
                        mapViewModel.updateSearchResponseObjectsRouting(searchItems,searchResultPlacemarkTapListener,ImageProvider.fromBitmap(mapViewModel.createBitmapFromVector(R.drawable.ic_consumer_24)))
                    }

                    if (successSearchState?.zoomToItems == true) {
                        mapViewModel.searchFocusCamera(
                            searchItems.map { item -> item.point },
                            successSearchState.itemsBoundingBox

                        )
                    }
                }

                SearchState.Loading -> mapViewModel.changeStatusProgressIndicator(ProgressIndicator.SHOW)
            }
        }
        LaunchedEffect(createRouteState) {
            when (createRouteState) {
                is SearchRouteState.Error -> mapViewModel.changeStatusProgressIndicator(
                    ProgressIndicator.HIDE
                )

                SearchRouteState.Loading -> mapViewModel.changeStatusProgressIndicator(
                    ProgressIndicator.SHOW
                )

                SearchRouteState.Off -> {}
                is SearchRouteState.Success -> {
                    mapViewModel.changeStatusProgressIndicator(ProgressIndicator.HIDE)
                    val successSearchRouteState = createRouteState as? SearchRouteState.Success
                    val searchItems = successSearchRouteState?.drivingRoutes ?: emptyList()
                    mapViewModel.onRoutesUpdated(searchItems)
                    searchItems.forEach { item ->
                        mapViewModel.routingFocusCamera(
                            item.geometry.points,
                            Polyline(item.geometry.points)
                        )
                    }
                }
            }
        }
    }
    when (showSheet) {
        BottomSheetState.HideBottomSheetState -> {

        }
        is BottomSheetState.ShowBottomSheetRoutingState -> {
            detailsVewModel.setDrivingRoute(showSheet.selectedObject)
            DetailsSheetRouting(detailsVewModel = detailsVewModel) {
                mapViewModel.changeStateSheet(BottomSheetState.HideBottomSheetState)
                mapViewModel.setDefaultColorRoutes()
            }
        }

        is BottomSheetState.ShowBottomSheetSearchState -> {
            detailsVewModel.setSelectedGeoObject((showSheet.electedObject))
            DetailsSheetSearch(detailsVewModel = detailsVewModel) {
                mapViewModel.changeStateSheet(BottomSheetState.HideBottomSheetState)
            }
        }
    }
}