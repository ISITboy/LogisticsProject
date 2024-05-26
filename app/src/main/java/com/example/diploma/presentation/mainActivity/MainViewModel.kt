package com.example.diploma.presentation.mainActivity

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.diploma.presentation.navigation.Destination
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.mapview.MapView
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _startDestination = mutableStateOf(Destination.SplashNavigation.route)
    val startDestination: State<String> = _startDestination

    fun onNavigationEvent(navigationState: NavigationState) {
        when (navigationState) {
            NavigationState.AuthorizationNavigation -> _startDestination.value =
                Destination.AuthorizationNavigation.route

            NavigationState.SplashNavigation -> _startDestination.value =
                Destination.SplashNavigation.route

            NavigationState.TabsNavigation -> _startDestination.value =
                Destination.TabsNavigation.route
        }
    }

    @SuppressLint("StaticFieldLeak")
    private var _mapView: MapView? = null
    val mapView: MapView?
        get() = _mapView

    fun initializeMapView(context: Context) {
        if (_mapView == null) {
            MapKitFactory.initialize(context)
            _mapView = MapView(context)
        }
    }

    fun onStart() {
        _mapView?.onStart()
        MapKitFactory.getInstance().onStart()
    }

    fun onStop() {
        _mapView?.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onCleared() {
        _mapView?.onStop()
        _mapView = null
        super.onCleared()
    }
}