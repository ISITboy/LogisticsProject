package com.example.diploma.domain.repository

import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectTapListener

interface MapKitRoutingListener {
    val routingTapListener:MapObjectTapListener
//    val routingInputListener:InputListener
    fun routingFocusCamera(points: List<Point>, polyline: Polyline)
}