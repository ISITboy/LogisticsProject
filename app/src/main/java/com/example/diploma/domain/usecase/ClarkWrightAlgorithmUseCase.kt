package com.example.diploma.domain.usecase

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.diploma.data.remote.dto.distances.Query
import com.example.diploma.domain.algorithm.ClarkWright
import com.example.diploma.domain.algorithm.getKilometerWinningMatrix
import com.example.diploma.domain.algorithm.getShortedDistanceMatrix
import com.example.diploma.domain.algorithm.makeMatrixWithoutGO
import com.example.diploma.domain.algorithm.models.ResultedClarkeAlgo
import com.example.diploma.domain.models.Consumer
import com.example.diploma.domain.models.Producer
import com.example.diploma.domain.models.Transport
import com.example.diploma.domain.repository.OpenRouteServiceRepository
import com.example.diploma.presentation.ui.tabs.manager.addressed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class ClarkWrightAlgorithmUseCase @Inject constructor(
    private val openRouteServiceRepository: OpenRouteServiceRepository,
) {

    private val _distances = MutableStateFlow<List<List<Double>>?>(null)
    private val resultedClarkeData = MutableLiveData<List<ResultedClarkeAlgo>>()
    fun getResultedClarkeData(): LiveData<List<ResultedClarkeAlgo>> = resultedClarkeData


    fun sendDataToServer(points: List<Double>, coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            try {

                val response = openRouteServiceRepository.calculateDistance(
                    Query(
                        locations = points.chunked(2),
                        metrics = listOf("distance")
                    )
                )
                _distances.emit(response.data?.distances)
            } catch (e: Exception) {
                _distances.emit(null)
            }
        }
    }

    fun startClarkWrightAlgorithm(
        producers: List<Producer>,
        consumers: List<Consumer>,
        transports: List<Transport>,
        coroutineScope: CoroutineScope
    ) {
        coroutineScope.launch {
            _distances.collectLatest { distanceMatrix ->
                distanceMatrix?.let { dist ->
                    val formattedDistanceMatrix = getFormattedDistanceMatrix(dist)
                    val resultedMatrix = getMatrixWithoutGO(getShortedDistanceMatrix(formattedDistanceMatrix, producers.size))
                    val algo = ClarkWright(
                        resultedMatrix,
                        getShortedDistanceMatrix(formattedDistanceMatrix, producers.size),
                        consumers.map { it.volume.toDouble() }.toMutableList(),
                        transports
                    )
                    algo.setMaxTonnage(getSelectedTransport(transports))
                    algo.startMethod()
                    resultedClarkeData.postValue(algo._routes)
                }
            }
        }
    }

    private fun getFormattedDistanceMatrix(distanceMatrix: List<List<Double>>) = distanceMatrix.map { it.toTypedArray() }.toTypedArray()
    private fun getMatrixWithoutGO(shortedDistanceMatrix: MutableList<Array<Array<Double>>>) = shortedDistanceMatrix.map { makeMatrixWithoutGO(getKilometerWinningMatrix(it)) }
    private fun getSelectedTransport(transports: List<Transport>) = transports.first { it.selected == 1 }.volume.toDouble()
}
