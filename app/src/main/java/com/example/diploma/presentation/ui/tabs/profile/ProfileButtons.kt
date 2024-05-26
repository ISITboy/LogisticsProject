package com.example.diploma.presentation.ui.tabs.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp

@Composable
fun EditButton(
    modifier:Modifier = Modifier,
    onEditClick:()->Unit
){
    Button(
        modifier = modifier.fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Black),
        shape = RectangleShape,
        onClick = {onEditClick()},
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) { Text(text = "Редактировать")}
}

@Composable
fun SaveButton(
    modifier:Modifier = Modifier,
    onSaveClick:()->Unit
){
    Button(
        modifier = modifier.fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Black),
        shape = RectangleShape,
        onClick = {onSaveClick()},
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(red = 0, green = 87, blue = 0, alpha = 255)
        )
    ) { Text(text = "Сохранение")}
}