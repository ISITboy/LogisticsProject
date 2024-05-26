package com.example.diploma.presentation.ui.tabs.mapkit.detailsSheet

import androidx.lifecycle.ViewModel
import com.example.diploma.presentation.Utils.takeIfNotEmpty
import com.yandex.mapkit.GeoObject
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.search.BusinessObjectMetadata
import com.yandex.mapkit.search.ToponymObjectMetadata
import com.yandex.mapkit.uri.UriObjectMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class DetailsSheetViewModel @Inject constructor() : ViewModel() {
    private var selectedObject: GeoObject? = null
    private var selectedRoute:DrivingRoute?=null

    fun setSelectedGeoObject(selectedObject: GeoObject?) {
        this.selectedObject = selectedObject
    }

    fun setDrivingRoute(selectedRoute:DrivingRoute?){
        this.selectedRoute = selectedRoute
    }

    private fun getUri() =
        selectedObject?.metadataContainer?.getItem(UriObjectMetadata::class.java)?.uris?.firstOrNull()

    fun uiSearchState(): DetailsDialogSearchUiState? {
        if (selectedObject == null) return null
        val geoObjetTypeUiState = selectedObject?.metadataContainer?.getItem(ToponymObjectMetadata::class.java)?.let {
            TypeSpecificState.Toponym(address = it.address.formattedAddress)
        } ?: selectedObject?.metadataContainer?.getItem(BusinessObjectMetadata::class.java)?.let {
            TypeSpecificState.Business(
                name = it.name,
                workingHours = it.workingHours?.text,
                categories = it.categories.map { it.name }.takeIfNotEmpty()?.toSet()
                    ?.joinToString(", "),
                phones = it.phones.map { it.formattedNumber }.takeIfNotEmpty()?.joinToString(", "),
                link = it.links.firstOrNull()?.link?.href,
            )
        } ?: TypeSpecificState.Undefined

        return DetailsDialogSearchUiState(
            title = selectedObject?.name ?: "No title",
            descriptionText = selectedObject?.descriptionText ?: "No description",
            location = selectedObject?.geometry?.firstOrNull()?.point,
            uri = getUri()?.value,
            typeSpecificState = geoObjetTypeUiState,
        )
    }

    fun uiRoutingState():DetailsDialogRoutingUiState?{
        return if (selectedRoute == null) null
        else DetailsDialogRoutingUiState(
            distance = selectedRoute?.metadata?.weight?.distance?.text ?:"",
            time = selectedRoute?.metadata?.weight?.time?.text ?:"",
            railwayCrossingsSize = selectedRoute?.railwayCrossings?.size.toString(),
            pedestrianCrossingsSize = selectedRoute?.pedestrianCrossings?.size.toString(),
            speedBumpsSize = selectedRoute?.speedBumps?.size.toString()
        )
    }

    data class DetailsDialogRoutingUiState(
        val distance:String,
        val time :String,
        val railwayCrossingsSize :String,
        val pedestrianCrossingsSize :String,
        val speedBumpsSize :String
    )

    data class DetailsDialogSearchUiState(
        val title: String,
        val descriptionText: String,
        val location: Point?,
        val uri: String?,
        val typeSpecificState: TypeSpecificState,
    )

    sealed interface TypeSpecificState {
        data class Toponym(val address: String) : TypeSpecificState

        data class Business(
            val name: String,
            val workingHours: String?,
            val categories: String?,
            val phones: String?,
            val link: String?,
        ) : TypeSpecificState

        object Undefined : TypeSpecificState
    }
}