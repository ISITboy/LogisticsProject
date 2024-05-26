package com.example.diploma.domain.repository

import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener

interface MapKitSearchListener {
//    val searchInputListener: InputListener
    fun searchFocusCamera(points: List<Point>, boundingBox: BoundingBox)
//    val searchResultPlacemarkTapListener: MapObjectTapListener
}
