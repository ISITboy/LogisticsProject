package com.example.diploma.data.impl.repository

import android.util.Log
import com.example.diploma.domain.models.state.SearchState
import com.example.diploma.domain.repository.MapKitSearchRepository
import com.example.diploma.presentation.ui.tabs.mapkit.SearchResponseItem
import com.yandex.mapkit.geometry.BoundingBox
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.map.VisibleRegion
import com.yandex.mapkit.map.VisibleRegionUtils
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.mapkit.search.Session
import com.yandex.runtime.Error
import com.yandex.runtime.network.NetworkError
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Queue
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

class MapKitSearchRepositoryImpl @Inject constructor() : MapKitSearchRepository {

    private val searchManager =
        SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
    private var searchSession: Session? = null
    private val region = MutableStateFlow<VisibleRegion?>(null)

    @OptIn(FlowPreview::class)
    private val throttledRegion = region.debounce(1.seconds)

    private val state = MutableStateFlow<SearchState>(SearchState.Off)
    private var zoomToSearchResult = false
    private var pointCollection: MapObjectCollection? = null
    override fun getSearchState(): MutableStateFlow<SearchState> = state

    override fun setVisibleRegion(region: VisibleRegion?) {
        this.region.value = region
    }

    override fun getPointCollection(): MapObjectCollection? = pointCollection

    override fun createSession(query: String, searchOptions: SearchOptions) {
        val region = region.value?.let {
            VisibleRegionUtils.toPolygon(it)
        } ?: return
        searchSession?.cancel()
        searchSession = searchManager.submit(
            query,
            region,
            searchOptions,
            searchSessionListener
        )
        state.value = SearchState.Loading
        zoomToSearchResult = true
    }

    override fun createSession(point: Point, searchOptions: SearchOptions) {
        searchSession?.cancel()
        searchSession = searchManager.submit(
            point,
            10,
            searchOptions,
            searchSessionListener
        )
        state.value = SearchState.Loading
        zoomToSearchResult = true
    }

    override fun setMapObjectCollection(mapObjectCollection: MapObjectCollection) {
        pointCollection = mapObjectCollection.addCollection()
    }

    private val searchSessionListener = object : Session.SearchListener {
        override fun onSearchResponse(response: Response) {
            val items = response.collection.children.mapNotNull {
                val point = it.obj?.geometry?.firstOrNull()?.point ?: return@mapNotNull null
                SearchResponseItem(point, it.obj)
            }
            Log.d("micha", "before boundingBox")
            val boundingBox = response.metadata.boundingBox ?: return
            Log.d("micha", "after boundingBox")

            state.value = SearchState.Success(
                items = items,
                zoomToSearchResult,
                itemsBoundingBox = boundingBox
            )
            Log.d("micha", "onSearchResponse ${state.value}")
        }

        override fun onSearchError(error: Error) {
            when (error) {
                is NetworkError -> state.value =
                    SearchState.Error("Search request error due network issues")

                else -> state.value = SearchState.Error("Search request unknown error")
            }
        }

    }

    override fun createSession(query: Queue<String>, searchOptions: SearchOptions) {
        CoroutineScope(Dispatchers.IO).launch {
            while (query.isNotEmpty()) {
                if (state.value is SearchState.Off) {
                    withContext(Dispatchers.Main) {
                        Log.d("micha", "after boundingBox")
                        searchManager.submit(
                            query.poll(),
                            Geometry.fromBoundingBox(
                                BoundingBox(
                                    Point(0.0, 0.0),
                                    Point(90.0, 180.0)
                                )
                            ),
                            searchOptions,
                            searchSessionListener
                        )
                        state.value = SearchState.Loading
                    }
                }
            }
        }
    }


    @OptIn(FlowPreview::class)
    override fun subscribeForSearch(): Flow<*> {
        Log.d("MyLog", "subscribeForSearch")
        return throttledRegion.filter { it != null }
            .filter { state.value is SearchState.Success }
            .mapNotNull { it }
            .debounce(100)
            .onEach { region ->
                searchSession?.let {
                    it.setSearchArea(VisibleRegionUtils.toPolygon(region))
                    it.resubmit(searchSessionListener)
                    state.value = SearchState.Loading
                    zoomToSearchResult = false
                }
            }
    }


}