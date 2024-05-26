package com.example.diploma.presentation.ui.authorization.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.diploma.R

@Composable
fun EmailInputLayout(
    modifier: Modifier = Modifier,
    email: State<String>,
    invalid:MutableState<Boolean>,
    enabled:Boolean = true,
    onChangeEmail:(String)->Unit
) {

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(0.8f),
        value = email.value,
        onValueChange = { onChangeEmail(it); invalid.value  = false },
        label = { if(enabled) Text(text = stringResource(id = R.string.text_input_email)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        trailingIcon = {
            if (email.value.isNotBlank() && enabled) {
                IconButton(onClick = { onChangeEmail("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = null)
                }
            }
        },
        singleLine = true,
        isError = invalid.value,
        supportingText = {
            if(invalid.value) Text("Проверьте email") else null
        },
        enabled = enabled
    )
}
