package com.example.diploma.presentation.ui.tabs.mapkit.detailsSheet

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsSheetSearch(
    modifier: Modifier = Modifier,
    detailsVewModel: DetailsSheetViewModel,
    onBottomSheet: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onBottomSheet) {
        detailsVewModel.uiSearchState()?.let {
            Text(text = it.title)
            Text(text = it.descriptionText)
            Text(text = "${it.location?.latitude}, ${it.location?.longitude}")
            if (it.uri != null) Text(text = it.title)
            when (val state = it.typeSpecificState) {
                is DetailsSheetViewModel.TypeSpecificState.Business -> {
                    Text(text = "Business organisation:")
                    Text(text = state.name)
                    if (state.workingHours != null) Text(text = state.workingHours)
                    state.categories?.let { categories -> Text(text = categories) }
                    state.phones?.let { phones -> Text(text = phones) }
                    state.link?.let { link -> Text(text = link) }
                }

                is DetailsSheetViewModel.TypeSpecificState.Toponym -> {
                    Text(text = "Toponym:")
                    Text(text = state.address)
                }
                DetailsSheetViewModel.TypeSpecificState.Undefined -> {
                    Text(text = "Undefined")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsSheetRouting(
    modifier: Modifier = Modifier,
    detailsVewModel: DetailsSheetViewModel,
    onBottomSheet: () -> Unit,
) {
    ModalBottomSheet(onDismissRequest = onBottomSheet) {
        detailsVewModel.uiRoutingState()?.let {
            Text(text= it.distance)
            Text(text= it.time)
            Text(text= it.speedBumpsSize)
            Text(text= it.railwayCrossingsSize)
            Text(text= it.pedestrianCrossingsSize)
        }
    }
}
