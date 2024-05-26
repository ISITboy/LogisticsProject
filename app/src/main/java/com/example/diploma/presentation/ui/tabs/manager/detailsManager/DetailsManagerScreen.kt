package com.example.diploma.presentation.ui.tabs.manager.detailsManager

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.diploma.presentation.ui.tabs.manager.detailsManager.components.DetailsManagerCompose


@Composable
fun DetailsManagerScreen(modifier: Modifier = Modifier, viewModel: DetailsManagerViewModel, onCloseDetailsManager:()->Unit) {
    val producers = viewModel.producers.observeAsState()
    val consumers = viewModel.consumer.observeAsState()
    val transports = viewModel.transport.observeAsState()

    if (producers.value == null || consumers.value == null || transports.value ==null){
        CircularProgressIndicator(modifier = Modifier.fillMaxSize().size(25.dp))
    }else{
        DetailsManagerCompose(
            producers = producers.value ?: listOf(),
            consumers = consumers.value ?: listOf(),
            transports = transports.value ?: listOf(),
            event = viewModel::eventManager,
            onSelectedTransport = {
                viewModel.clearColumnSelected()
                viewModel.selectTransport(it)
            },
            makeRoute = onCloseDetailsManager
        )
    }

}