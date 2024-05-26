package com.example.diploma.domain.models

import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.FilterCollection
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.SearchType

class SearchOptionsBuilder {

    private var searchTypes = SearchType.NONE.value
    private var geometry = false
    private var disableSpellingCorrection = false
    private var resultPageSize :Int? = 10
    private var userPosition : Point?= null
    private var origin : String? = null
    private var filters :FilterCollection?= null

    fun setSearchTypes(searchTypes:SearchType):SearchOptionsBuilder{
        this.searchTypes = searchTypes.value
        return this
    }
    fun setGeometry(geometry:Boolean):SearchOptionsBuilder{
        this.geometry = geometry
        return this
    }
    fun setDisableSpellingCorrection(disableSpellingCorrection:Boolean):SearchOptionsBuilder{
        this.disableSpellingCorrection = disableSpellingCorrection
        return this
    }
    fun setResultPageSize(resultPageSize:Int?):SearchOptionsBuilder{
        this.resultPageSize = resultPageSize
        return this
    }
    fun setUserPosition(userPosition:Point?):SearchOptionsBuilder{
        this.userPosition = userPosition
        return this
    }
    fun setOrigin(origin:String?):SearchOptionsBuilder{
        this.origin = origin
        return this
    }
    fun setFilters(filters:FilterCollection?):SearchOptionsBuilder{
        this.filters = filters
        return this
    }

    fun build()= SearchOptions(
        searchTypes,
        resultPageSize,
        userPosition,
        origin,
        geometry,
        disableSpellingCorrection,
        filters
    )
}