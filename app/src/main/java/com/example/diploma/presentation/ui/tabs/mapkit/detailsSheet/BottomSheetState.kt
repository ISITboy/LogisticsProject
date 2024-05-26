package com.example.diploma.presentation.ui.tabs.mapkit.detailsSheet

import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.directions.driving.DrivingRoute

sealed class BottomSheetState(){
    data class ShowBottomSheetRoutingState(val selectedObject: DrivingRoute) : BottomSheetState()
    data class ShowBottomSheetSearchState(val electedObject: GeoObject?) : BottomSheetState()
    data object HideBottomSheetState : BottomSheetState()
}