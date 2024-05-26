package com.example.diploma.presentation.ui.tabs.manager.detailsManager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diploma.domain.models.Consumer
import com.example.diploma.domain.models.Producer
import com.example.diploma.domain.models.SearchOptionsBuilder
import com.example.diploma.domain.models.Transport
import com.example.diploma.domain.usecase.ConsumerUseCase
import com.example.diploma.domain.usecase.ProducerUseCase
import com.example.diploma.domain.usecase.TransportUseCase
import com.example.diploma.domain.usecase.mapkit.SearchUseCase
import com.yandex.mapkit.search.SearchOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Queue
import javax.inject.Inject

@HiltViewModel
class DetailsManagerViewModel @Inject constructor(
    private val producerUseCase: ProducerUseCase,
    private val consumerUseCase: ConsumerUseCase,
    private val transportUseCase: TransportUseCase
) : ViewModel() {

    init {
        getAllConsumers()
        getAllProducers()
        getAllTransports()
    }

    private val _producers = MutableLiveData<List<Producer>>()
    val producers = _producers
    private val _consumers = MutableLiveData<List<Consumer>>()
    val consumer = _consumers
    private val _transports = MutableLiveData<List<Transport>>()
    val transport = _transports


    private fun insertProducer(producer: Producer) = viewModelScope.launch {
        producerUseCase.insert(producer)
    }

    private fun updateProducer(producer: Producer) = viewModelScope.launch {
        producerUseCase.update(producer)
    }

    private fun deleteProducer(producer: Producer) = viewModelScope.launch {
        producerUseCase.delete(producer)
    }

    private fun getAllProducers() = viewModelScope.launch {
        producerUseCase.getAllProducer().collect{
            _producers.postValue(it)
        }
    }

    private fun insertConsumer(consumer: Consumer) = viewModelScope.launch {
        consumerUseCase.insert(consumer)
    }

    private fun updateConsumer(consumer: Consumer) = viewModelScope.launch {
        consumerUseCase.update(consumer)
    }

    private fun deleteConsumer(consumer: Consumer) = viewModelScope.launch {
        consumerUseCase.delete(consumer)
    }

    private fun getAllConsumers() = viewModelScope.launch {
        consumerUseCase.getAllConsumers().collect{
            _consumers.postValue(it)
        }
    }

    private fun insertTransport(transport: Transport) = viewModelScope.launch {
        transportUseCase.insert(transport)
    }

    private fun updateTransport(transport: Transport) = viewModelScope.launch {
        transportUseCase.update(transport)
    }

    private fun deleteTransport(transport: Transport) = viewModelScope.launch {
        transportUseCase.delete(transport)
    }

    private fun getAllTransports() = viewModelScope.launch {
        transportUseCase.getAllTransports().collect{
            _transports.postValue(it)
        }
    }

    fun eventManager(event: MembersEvent){
        when(event){
            is MembersEvent.AddConsumerItem -> insertConsumer(event.consumer)
            is MembersEvent.AddProducerItem -> insertProducer(event.producer)
            is MembersEvent.DeleteConsumerItem -> deleteConsumer(event.consumer)
            is MembersEvent.DeleteProducerItem -> deleteProducer(event.producer)
            is MembersEvent.UpdateConsumerItem -> updateConsumer(event.consumer)
            is MembersEvent.UpdateProducerItem -> updateProducer(event.producer)
            is MembersEvent.AddTransportItem -> insertTransport(event.transport)
            is MembersEvent.DeleteTransportItem -> deleteTransport(event.transport)
            is MembersEvent.UpdateTransportItem -> updateTransport(event.transport)
        }
    }

    fun clearColumnSelected()= viewModelScope.launch {
        transportUseCase.clearColumnSelected()
    }


    fun selectTransport(id: Int)= viewModelScope.launch {
        transportUseCase.selectTransport(id)
    }


}