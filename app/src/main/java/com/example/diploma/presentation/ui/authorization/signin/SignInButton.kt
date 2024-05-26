package com.example.diploma.presentation.ui.authorization.signin

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
fun SignInButton (modifier: Modifier= Modifier,onSigIn:()->Unit){
    Button(
        modifier = modifier.fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Black),
        shape = RectangleShape,
        onClick = {onSigIn()}
    ) {
        Text(text = "Войти")
    }
}

@Composable
fun GoToRegistrationScreen(modifier: Modifier= Modifier,onGoToRegistrationScreen:()->Unit){
    Button(
        modifier = modifier.fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Blue),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        onClick = {onGoToRegistrationScreen()}
    ) {
        Text(text = "Зарегистрироваться")
    }
}