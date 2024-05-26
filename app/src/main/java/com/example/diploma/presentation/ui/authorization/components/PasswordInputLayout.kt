package com.example.diploma.presentation.ui.authorization.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.example.diploma.R

@Composable
fun PasswordInputLayout(
    modifier: Modifier = Modifier,
    password: State<String>,
    invalid:MutableState<Boolean>,
    enabled:Boolean = true,
    onChangePassword:(String)->Unit
) {
    var visible by remember { mutableStateOf(false) }
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(0.8f),
        value = password.value,
        onValueChange = { onChangePassword(it); invalid.value = false },
        label = { if(enabled) Text(text = stringResource(id = R.string.text_input_password)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        trailingIcon = {
            if (password.value.isNotBlank()&& enabled) {
                IconButton(onClick = { visible = !visible }) {
                    Icon(
                        painter = if (visible) painterResource(id = R.drawable.ic_invisibility) else painterResource(
                            id = R.drawable.ic_visibility
                        ),
                        contentDescription = null
                    )
                }
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        isError = invalid.value,
        supportingText = {
            if(invalid.value) Text("Минимум 8 символов") else null
        },
        enabled = enabled
    )
}