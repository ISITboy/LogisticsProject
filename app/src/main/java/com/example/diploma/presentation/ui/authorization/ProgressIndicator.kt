package com.example.diploma.presentation.ui.authorization

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator(
) {

    Surface(
        color = Color.Black.copy(alpha = 0.2f), // Прозрачность 20%
        modifier = Modifier
            .fillMaxSize()
            .clickable { }
    ) {
        // Прогресс-бар
        Box(
            modifier = Modifier
                .padding(16.dp)
                .wrapContentSize(Alignment.Center)
        ) {
            CircularProgressIndicator(
                color = Color.White, // Цвет прогресс-бара
                modifier = Modifier.size(50.dp)
            )
        }
    }

}