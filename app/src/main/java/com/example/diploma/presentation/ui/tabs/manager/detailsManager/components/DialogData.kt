package com.example.diploma.presentation.ui.tabs.manager.detailsManager.components

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.diploma.domain.models.Transport

data class MemberDialogData(
    val text :MutableState<String>,
    val volume :MutableState<String>,
    val invalid: MutableState<Boolean>,
)

data class TransportDialogData(
    val id : MutableState<Int> = mutableStateOf(0),
    val name :MutableState<String>,
    val volume :MutableState<String>,
    val height :MutableState<String> = mutableStateOf(""),
    val width :MutableState<String> = mutableStateOf(""),
    val length :MutableState<String> = mutableStateOf(""),
    val weight :MutableState<String> = mutableStateOf(""),
    val selected :MutableState<Int> = mutableStateOf(0),
    val invalid: MutableState<Boolean> = mutableStateOf(false),
)

fun TransportDialogData.mapInTransport() =
    Transport(
        id = this.id.value,
        name = this.name.value,
        volume = this.volume.value,
        height = this.height.value,
        width = this.width.value,
        length = this.length.value,
        weight = this.weight.value,
        selected = this.selected.value
    )
