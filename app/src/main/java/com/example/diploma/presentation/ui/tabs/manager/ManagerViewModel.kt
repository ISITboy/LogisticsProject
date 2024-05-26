package com.example.diploma.presentation.ui.tabs.manager

import android.util.Log
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.diploma.domain.algorithm.models.ResultedClarkeAlgo
import com.example.diploma.domain.models.Consumer
import com.example.diploma.domain.models.Producer
import com.example.diploma.domain.models.SearchOptionsBuilder
import com.example.diploma.domain.models.Transport
import com.example.diploma.domain.usecase.ClarkWrightAlgorithmUseCase
import com.example.diploma.domain.usecase.ConsumerUseCase
import com.example.diploma.domain.usecase.ProducerUseCase
import com.example.diploma.domain.usecase.TransportUseCase
import com.example.diploma.domain.usecase.mapkit.SearchUseCase
import com.example.diploma.presentation.navigation.Destination
import com.example.diploma.presentation.ui.tabs.ProgressIndicator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject


@HiltViewModel
class ManagerViewModel @Inject constructor(
    private val producerUseCase: ProducerUseCase,
    private val consumerUseCase: ConsumerUseCase,
    private val transportUseCase: TransportUseCase,
    private val clarkWrightAlgorithmUseCase: ClarkWrightAlgorithmUseCase,
    private val searchUseCase: SearchUseCase
):ViewModel() {
    init {
        getAllConsumers()
        getAllProducers()
        getAllTransports()
    }

    private val _statusProgressIndicator = MutableStateFlow(ProgressIndicator.HIDE)
    val statusProgressIndicator: StateFlow<ProgressIndicator> =
        _statusProgressIndicator.asStateFlow()
    fun changeStatusProgressIndicator(status: ProgressIndicator) {
        _statusProgressIndicator.value = status
    }

    val searchState = searchUseCase.getSearchState()

    fun createSession() = viewModelScope.launch{
        val shippers = _producers.value?.map { it.address } ?: emptyList()
        val consignees = _consumers.value?.map { it.address } ?: emptyList()
        val addresses = shippers + consignees
        val queue: Queue<String> = LinkedList(addresses)
        Log.d("MyLog","addressed ${queue.size}")
        searchUseCase.createSession(queue, SearchOptionsBuilder().build())
    }

    private val _producers = MutableLiveData<List<Producer>>()
    val producers = _producers
    private val _consumers = MutableLiveData<List<Consumer>>()
    val consumers = _consumers
    private val _transports = MutableLiveData<List<Transport>>()
    val transports = _transports

    val resultedClarkeAlgo = clarkWrightAlgorithmUseCase.getResultedClarkeData()

    fun sendDataToServer(points: List<Double>){
        clarkWrightAlgorithmUseCase.sendDataToServer(points,viewModelScope)
    }
    fun startClarkWrightAlgorithm(
        producers: List<Producer>,
        consumers: List<Consumer>,
        transports: List<Transport>
    ){
        clarkWrightAlgorithmUseCase.startClarkWrightAlgorithm(producers,consumers,transports,viewModelScope)
    }

    private fun getAllProducers() = viewModelScope.launch {
        producerUseCase.getAllProducer().collect{
            _producers.postValue(it)
        }
    }
    private fun getAllConsumers() = viewModelScope.launch {
        consumerUseCase.getAllConsumers().collect{
            _consumers.postValue(it)
        }
    }

    private fun getAllTransports() = viewModelScope.launch {
        transportUseCase.getAllTransports().collect{
            _transports.postValue(it)
        }
    }

    fun onCreateRoute(resultedData: ResultedClarkeAlgo, addressed: MutableList<Double>, openAndPopUp: (String, String) -> Unit) {
        val chunked = addressed.chunked(2)
        val points = mutableListOf(chunked[resultedData.idConsignee].last(),chunked[resultedData.idConsignee].first())
        resultedData.routes.forEach {
            points.add(chunked[it+producers.value?.size!!].last())
            points.add(chunked[it+producers.value?.size!!].first())
        }
        points.add(chunked[resultedData.idConsignee].last())
        points.add(chunked[resultedData.idConsignee].first())
        val route = points.joinToString(separator = ",").ifEmpty { "empty" }
        openAndPopUp("${Destination.MapKitDestination.route}/${route}", Destination.ManagerDestination.route)
    }

}