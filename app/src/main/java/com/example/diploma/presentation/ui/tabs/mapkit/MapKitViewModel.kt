package com.example.diploma.presentation.ui.tabs.mapkit

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diploma.R
import com.example.diploma.domain.models.DrivingOptionsBuilder
import com.example.diploma.domain.models.SearchOptionsBuilder
import com.example.diploma.domain.models.state.SearchRouteState
import com.example.diploma.domain.repository.MapKitRoutingListener
import com.example.diploma.domain.repository.MapKitSearchListener
import com.example.diploma.domain.usecase.mapkit.RoutingUseCase
import com.example.diploma.domain.usecase.mapkit.SearchUseCase
import com.example.diploma.presentation.ui.tabs.ProgressIndicator
import com.example.diploma.presentation.ui.tabs.mapkit.detailsSheet.BottomSheetState
import com.yandex.mapkit.Animation
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.IconStyle
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.MapObjectTapListener
import com.yandex.mapkit.map.PolylineMapObject
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType
import com.yandex.runtime.image.ImageProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Queue
import javax.inject.Inject

@HiltViewModel
class MapKitViewModel @Inject constructor(
    val searchUseCase: SearchUseCase,
    val routingUseCase: RoutingUseCase,
    @ApplicationContext private val context: Context
) : ViewModel(), MapKitRoutingListener, MapKitSearchListener {
    var map: Map? = null


    private val _showSheet: MutableStateFlow<BottomSheetState> =
        MutableStateFlow(BottomSheetState.HideBottomSheetState)
    val showSheet: StateFlow<BottomSheetState> = _showSheet.asStateFlow()
    fun changeStateSheet(status: BottomSheetState) {
        _showSheet.value = status
    }

    private val _keySubscribeForSearch = MutableStateFlow(false)
    val keySubscribeForSearch: StateFlow<Boolean> = _keySubscribeForSearch.asStateFlow()
    fun changeKeySubscribeForSearch() {
        _keySubscribeForSearch.value = !_keySubscribeForSearch.value
        Log.d("MyLog", "_keySubscribeForSearch ${_keySubscribeForSearch.value}")
    }

    private val _activeSubscribeForSearch = MutableStateFlow(false)
    val activeSubscribeForSearch: StateFlow<Boolean> = _activeSubscribeForSearch.asStateFlow()

    fun clearMapObjects() {
        searchUseCase.getSearchPointCollection()?.clear()
    }

    private val _searchedObject = MutableStateFlow("")
    val searchedObject: StateFlow<String> = _searchedObject.asStateFlow()

    private val _invalid = MutableStateFlow(false)
    val invalid: StateFlow<Boolean> = _invalid.asStateFlow()

    private val _statusProgressIndicator = MutableStateFlow(ProgressIndicator.HIDE)
    val statusProgressIndicator: StateFlow<ProgressIndicator> =
        _statusProgressIndicator.asStateFlow()


    fun updateText(inputtedText: String) {
        if (_invalid.value) _invalid.value = false
        _searchedObject.value = inputtedText
    }

    fun startSearchObject() {
        if (_searchedObject.value.isNotEmpty()) {
            createSession(
                query = _searchedObject.value,
                searchOptions = SearchOptionsBuilder().setSearchTypes(SearchType.BIZ).build()
            )
        } else _invalid.value = true
    }

    fun changeStatusProgressIndicator(status: ProgressIndicator) {
        _statusProgressIndicator.value = status
    }

    fun containerButtonEvent(buttonEvent: ButtonEvent) {
        when (buttonEvent) {
            ButtonEvent.RemoveObjects -> clearMapObjects()
            ButtonEvent.RemoveRouts -> routingUseCase.clearRoutes()
            ButtonEvent.SelectSubscribeForSearch -> {
                _activeSubscribeForSearch.value =
                    !_activeSubscribeForSearch.value
            }
        }
    }

    fun getSearchState() = searchUseCase.getSearchState()

    fun setVisibleRegion(region: VisibleRegion?) {
        searchUseCase.setVisibleRegion(region)
    }

    private fun createSession(query: String, searchOptions: SearchOptions) {
        searchUseCase.createSession(query, searchOptions)
    }
    fun createSession(point: Point) {
        searchUseCase.createSession(point, SearchOptionsBuilder().setSearchTypes(SearchType.NONE).build())
    }

    private fun createSession(query: Queue<String>, searchOptions: SearchOptions) {
        searchUseCase.createSession(query, searchOptions)
    }

    private var currentJob: Job? = null

    //    fun subscribeForSearch(): Flow<*> = searchUseCase.subscribeForSearch()
    fun subscribeForSearch() = viewModelScope.launch {
        _activeSubscribeForSearch.collectLatest { isSubscribed ->
            currentJob?.cancel()
            if (isSubscribed) {
                currentJob = launch {
                    searchUseCase.subscribeForSearch().collect()
                }
            }
        }
    }


    fun setDrivingOptions() {
        routingUseCase.setDrivingOptions(DrivingOptionsBuilder().setRoutesCount(1).build())
    }

    fun setRouteTapListener(mapObjectTapListener: MapObjectTapListener) {
        routingUseCase.setRouteTapListener(mapObjectTapListener)
    }

    fun setVehicleOptions(vehicleOptions: VehicleOptions) {
        routingUseCase.setVehicleOptions(vehicleOptions)
    }

    fun createSessionCreateRoute() {
        routingUseCase.createSessionCreateRoute()
    }

    fun getRoutingState(): MutableStateFlow<SearchRouteState> = routingUseCase.getRoutingState()
    fun setMapObjectCollection(mapObjectCollection: MapObjectCollection) {
        routingUseCase.setMapObjectCollection(mapObjectCollection)
        searchUseCase.setMapObjectCollection(mapObjectCollection)
    }


    private val styleMainRoute: PolylineMapObject.() -> Unit = {
        zIndex = 10f
        setStrokeColor(ContextCompat.getColor(context, R.color.color_rout))
        strokeWidth = 5f
        outlineColor = ContextCompat.getColor(context, R.color.color_border_rout)
        outlineWidth = 3f
    }

    private val styleAlternativeRoute: PolylineMapObject.() -> Unit = {
        zIndex = 5f
        setStrokeColor(ContextCompat.getColor(context, R.color.purple_500))
        strokeWidth = 4f
        outlineColor = ContextCompat.getColor(context, R.color.black)
        outlineWidth = 2f
    }

    fun onRoutesUpdated(routes: List<DrivingRoute>) {
        routingUseCase.onRoutesUpdated(routes, styleMainRoute, styleAlternativeRoute)
    }

    fun updateSearchResponsePlacemarks(
        items: List<SearchResponseItem>,
        tapListener: MapObjectTapListener
    ) {
        clearMapObjects()
        val searchIcon = ImageProvider.fromResource(context, R.drawable.search_result)

        items.forEach {
            searchUseCase.getSearchPointCollection()?.addPlacemark(
                it.point,
                searchIcon,
                IconStyle().apply { scale = 0.5f }
            )?.apply {
                addTapListener(tapListener)
                userData = it.geoObject
            }
        }
    }

    fun updateSearchResponseObjectsRouting(
        items: List<SearchResponseItem>,
        tapListener: MapObjectTapListener,
        imageProvider: ImageProvider
    ) {
        items.forEach {
            searchUseCase.getSearchPointCollection()?.addPlacemark(
                it.point,
                imageProvider,
                IconStyle().apply { scale = 0.5f }
            )?.apply {
                addTapListener(tapListener)
                userData = it.geoObject
            }
        }
    }


    fun addRequestPoint(point: Point) {
        routingUseCase.addRequestPoint(point)
    }

    fun createBitmapFromVector(art: Int): Bitmap? {
        val drawable = ContextCompat.getDrawable(context, art) ?: return null
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        ) ?: return null
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun setDefaultColorRoutes() {
        routingUseCase.getPolylinesMapsObjects().forEach {
            it?.setStrokeColor(ContextCompat.getColor(context, R.color.color_rout))
        }
    }


    override val routingTapListener: MapObjectTapListener
        get() = MapObjectTapListener { mapObject, _ ->
            setDefaultColorRoutes()

            val polyline = (mapObject as PolylineMapObject)
            polyline.setStrokeColor(ContextCompat.getColor(context, R.color.light_blue))

            val selectedObject = (mapObject.userData as DrivingRoute)

            _showSheet.value = BottomSheetState.ShowBottomSheetRoutingState(selectedObject)

            true
        }


    override fun routingFocusCamera(points: List<Point>, polyline: Polyline) {
        if (points.isEmpty()) return

        val position = if (points.size == 1) {
            map?.cameraPosition?.run {
                CameraPosition(points.first(), zoom, azimuth, tilt)
            }
        } else {
            map?.cameraPosition(Geometry.fromPolyline(polyline))
        }

        position?.let { map?.move(it, Animation(Animation.Type.SMOOTH, 0.5f), null) }
    }

    override fun searchFocusCamera(points: List<Point>, boundingBox: BoundingBox) {
        if (points.isEmpty()) return

        val position = if (points.size == 1) {
            map?.let {
                it.cameraPosition.run {
                    CameraPosition(points.first(), 15f, azimuth, tilt)
                }
            }
        } else {
            map?.cameraPosition(Geometry.fromBoundingBox(boundingBox))
        }
        position?.let {
            map?.move(position, Animation(Animation.Type.SMOOTH, 0.5f), null)
        }

    }

}