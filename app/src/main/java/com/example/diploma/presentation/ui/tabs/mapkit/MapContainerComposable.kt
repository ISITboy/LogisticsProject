package com.example.diploma.presentation.ui.tabs.mapkit

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.diploma.R
import com.example.diploma.presentation.ui.tabs.ProgressIndicator
import com.yandex.mapkit.mapview.MapView

@Composable
fun MapContainerComposable(
    modifier: Modifier = Modifier,
    mapView: MapView?,
    searchedObject: String,
    invalid: Boolean,
    mapViewModel: MapKitViewModel,
    showProgressIndicator: State<ProgressIndicator>,
    onEventButton: (ButtonEvent) -> Unit,
    activeSubscribeForSearch: Boolean,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center

    ) {
        mapView?.let {
            AndroidView(
                factory = { _ -> it },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )
        }

        InputSearchLayout(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.TopCenter)
                .padding(top = 5.dp),
            searchedObject = searchedObject,
            invalid = invalid,
            mapViewModel = mapViewModel
        )
        if (showProgressIndicator.value == ProgressIndicator.SHOW) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(35.dp)
                    .align(Alignment.TopEnd)
                    .padding(top = 15.dp, end = 5.dp)
            )
        }
        Card(
            modifier = Modifier
                .fillMaxWidth(0.45f)
                .align(Alignment.BottomStart)
                .padding(5.dp),
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.cardElevation(5.dp),
            border = BorderStroke(width = 3.dp, Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier.padding(2.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if(activeSubscribeForSearch) Color.White else Color.Transparent
                    )
                ) {
                    IconButton(onClick = { onEventButton(ButtonEvent.SelectSubscribeForSearch) }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_find_replace_24),
                            contentDescription = null
                        )
                    }
                }

                IconButton(
                    modifier = Modifier.padding(2.dp),
                    onClick = { onEventButton(ButtonEvent.RemoveRouts) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_remove_road),
                        contentDescription = null
                    )
                }
                IconButton(
                    modifier = Modifier.padding(2.dp),
                    onClick = { onEventButton(ButtonEvent.RemoveObjects) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_remove_point),
                        contentDescription = null
                    )
                }
            }
        }

        content()
    }

}

@Composable
fun InputSearchLayout(
    modifier: Modifier = Modifier,
    searchedObject: String,
    invalid: Boolean,
    mapViewModel: MapKitViewModel,
) {
    OutlinedTextField(
        modifier = modifier,
        value = searchedObject,
        onValueChange = mapViewModel::updateText,
        trailingIcon = {
            if (searchedObject.isNotBlank()) {
                IconButton(onClick = { mapViewModel.updateText("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        placeholder = { Text(text = stringResource(id = R.string.input_placeholder)) },
        leadingIcon = {
            IconButton(onClick = { mapViewModel.startSearchObject() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = { mapViewModel.startSearchObject() }
        ),
        singleLine = true,
        isError = invalid,
        supportingText = {
            if (invalid) Text("Пустое поле") else null
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White.copy(0.3f),
            unfocusedContainerColor = Color.White.copy(0.7f),
        ),
        shape = RoundedCornerShape(15.dp),
    )
}