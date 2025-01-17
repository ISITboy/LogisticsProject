package com.example.diploma.presentation.ui.tabs.manager.detailsManager.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.diploma.R
import com.example.diploma.domain.models.Transport

@Composable
fun TruckContainer(
    modifier: Modifier = Modifier,
    transports: List<Transport>,
    addTransportClick : () -> Unit,
    updateTransport:(Transport)->Unit,
    selectedTransport: (Int) -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }
    val slideOffset by animateDpAsState(
        targetValue = if (isVisible) 150.dp else (0).dp,
        animationSpec = tween(durationMillis = 300))
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            modifier = Modifier
                .size(60.dp)
                .clickable { addTransportClick() },
            painter = painterResource(id = R.drawable.ic_bus_80),
            contentDescription = null
        )
        LazyHorizontalGrid(
            rows = GridCells.Fixed(1),
            modifier = Modifier
                .height(60.dp)
                .width(slideOffset)
        ){
            items(transports) { transport ->
                TransportItem(
                    transport = transport,
                    updateTransportClick = {updateTransport(transport)},
                    selectedTransport = { selectedTransport(transport.id) }
                )
            }
        }
        Image(
            modifier = Modifier
                .size(40.dp)
                .clickable { isVisible = !isVisible },
            imageVector = if(isVisible)Icons.Default.KeyboardArrowLeft else Icons.Default.KeyboardArrowRight,
            contentDescription = null
        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TransportItem(modifier: Modifier = Modifier, transport: Transport, updateTransportClick:()->Unit,selectedTransport:()->Unit){
    Card(
        modifier = modifier.padding(horizontal = 3.dp).size(width = 60.dp, height = 100.dp)
            .combinedClickable(
                onDoubleClick = { updateTransportClick() },
                onClick = { selectedTransport() }
            ),
        shape = RoundedCornerShape(3.dp),
        elevation = CardDefaults.cardElevation(5.dp),
//        border = BorderStroke(width = if(transport.selected==0) (-1).dp else 1.dp, Color.White)
        border = if(transport.selected==0) BorderStroke(1.dp,Color.White) else BorderStroke(1.dp,Color.Green)
    ){
        Column(
            modifier = Modifier.fillMaxSize().background(colorResource(id = R.color.card_view_background)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(transport.name, fontSize = 15.sp ,modifier= Modifier.padding(horizontal = 2.dp),overflow = TextOverflow.Ellipsis, maxLines = 1)
            Spacer(modifier = Modifier.height(5.dp))
            Text(transport.volume, fontSize = 15.sp,modifier= Modifier.padding(horizontal = 2.dp),overflow = TextOverflow.Ellipsis,maxLines = 1)
            Spacer(modifier = Modifier.height(5.dp))
            Text(modifier = Modifier.align(Alignment.CenterHorizontally),text = "кг.", fontSize = 10.sp)
        }
    }

}
