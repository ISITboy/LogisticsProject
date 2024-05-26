package com.example.diploma.presentation.ui.tabs.manager

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.diploma.R
import com.example.diploma.domain.algorithm.models.ResultedClarkeAlgo
import com.example.diploma.domain.models.state.SearchState
import com.example.diploma.presentation.ui.tabs.ProgressIndicator

val addressed = mutableListOf<Double>()
@Composable
fun ManagerScreen(modifier: Modifier=Modifier, viewModel:ManagerViewModel, clickOpenDetailsManager:()->Unit, createRoute:(String,String)->Unit){
    val producers = viewModel.producers.observeAsState()
    val consumers = viewModel.consumers.observeAsState()
    val transports = viewModel.transports.observeAsState()
    val resultedClarkeData = viewModel.resultedClarkeAlgo.observeAsState()
    val searchState = viewModel.searchState.collectAsState().value

    if (producers.value != null && consumers.value != null && transports.value !=null){
        viewModel.startClarkWrightAlgorithm(producers.value!!,consumers.value!!,transports.value!!)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            ResultRowManager(onSelectedClick = {})
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
            ) {
                items(resultedClarkeData.value ?: listOf()) { resultedData ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(
                                colorResource(id = R.color.card_view_item_background)
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ResultRowManager(resultedClarkeAlgo = resultedData) {
                            viewModel.onCreateRoute(resultedData, addressed, openAndPopUp = createRoute)
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    addressed.clear()
                    viewModel.createSession()
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Построить")
            }
            Button(
                onClick = {
                    clickOpenDetailsManager()
                },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = "Детальнее")
            }
        }

        if (viewModel.statusProgressIndicator.value == ProgressIndicator.SHOW) {
            CircularProgressIndicator(modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center))
        }


    }

    LaunchedEffect(key1 = searchState, block = {
        Log.d("key1","$searchState")

        when (searchState) {
            is SearchState.Error -> {
                viewModel.changeStatusProgressIndicator(ProgressIndicator.HIDE)
            }
            SearchState.Off -> {}
            is SearchState.Success -> {
                val successSearchState = searchState as? SearchState.Success
                val searchItems = successSearchState?.items ?: emptyList()
                addressed.add(searchItems.first().point.longitude)
                addressed.add(searchItems.first().point.latitude)
                Log.d("MyLog","list addressed $addressed")
                if (addressed.size == (getSizeMembers(producers.value?.size,consumers.value?.size)) * 2) {
                    Log.d("MyLog","result addressed $addressed")
                    viewModel.sendDataToServer(addressed)
                    viewModel.changeStatusProgressIndicator(ProgressIndicator.HIDE)
                }
                viewModel.searchState.value = SearchState.Off
            }
            SearchState.Loading -> viewModel.changeStatusProgressIndicator(ProgressIndicator.SHOW)
        }
    })
}

private fun getSizeMembers(sizeProducer:Int?,sizeConsumers:Int?):Int{
    return if(sizeConsumers==null || sizeProducer==null) -1
    else sizeConsumers+sizeProducer
}

@Composable
fun ResultRowManager(
    modifier: Modifier = Modifier,
    resultedClarkeAlgo: ResultedClarkeAlgo? = null,
    onSelectedClick:()->Unit
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(colorResource(id = R.color.card_view_item_background))
            .clickable { onSelectedClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(modifier = Modifier
            .weight(0.1f), text = resultedClarkeAlgo?.transport?.name ?: "Транспорт", textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Text(modifier = Modifier
            .weight(0.3f), text = getRoute(resultedClarkeAlgo), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Text(modifier = Modifier
            .weight(0.2f), text = (resultedClarkeAlgo?.volume ?: "Объем перевозки, ldm").toString(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Text(modifier = Modifier
            .weight(0.2f), text = (resultedClarkeAlgo?.totalMileage ?: "Общий пробег, км").toString(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Text(modifier = Modifier
            .weight(0.2f), text = (resultedClarkeAlgo?.mileageWithoutLoad ?: "Пробег без груза, км").toString(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
    }
}

private fun getRoute(resultedClarkeAlgo: ResultedClarkeAlgo?):String{
    return if(resultedClarkeAlgo==null) "Маршрут"
    else "ГО${resultedClarkeAlgo.idConsignee} ${resultedClarkeAlgo.routes} ГО${resultedClarkeAlgo.idConsignee}"

}
