package com.example.diploma.presentation.ui.authorization.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.applicationquizforstudents.presentation.ui.screens.authorization.components.isValidEmail
import com.example.applicationquizforstudents.presentation.ui.screens.authorization.components.isValidPassword
import com.example.diploma.R
import com.example.diploma.domain.models.User
import com.example.diploma.domain.models.state.AuthResult
import com.example.diploma.presentation.ui.authorization.ProgressIndicator
import com.example.diploma.presentation.ui.authorization.components.EmailInputLayout
import com.example.diploma.presentation.ui.authorization.components.PasswordInputLayout
import com.example.diploma.presentation.ui.authorization.signin.SignInEvent

@Composable
fun SignUpScreen(
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel(),
    emailType:String,
    onEvent: (SignUpEvent) -> Unit
) {
    var errorMessage by remember {
        mutableStateOf("")
    }

    val email = viewModel.email.collectAsState()
    viewModel.updateEmail(if (emailType == "empty") email.value else emailType)
    val password = viewModel.password.collectAsState()
    val invalidPassword = remember { mutableStateOf(false) }
    val invalidEmail = remember { mutableStateOf(false) }
    val name = viewModel.name.collectAsState()

    when (viewModel.authState.collectAsState().value) {
        is AuthResult.Loading -> {
            ProgressIndicator()
        }

        is AuthResult.Nothing -> {}
        is AuthResult.Success -> {
            val result = viewModel.authState.collectAsState().value as AuthResult.Success
            viewModel.createUser(
                User.Base(
                    id = result.user.id,
                    email = result.user.email,
                    name = viewModel.name.value,
                )
            )
        }

        is AuthResult.Error -> {
            errorMessage =
                (viewModel.authState.collectAsState().value as AuthResult.Error).e.message.toString()
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.header_registration_screen),
            style = MaterialTheme.typography.headlineLarge
        )
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        EmailInputLayout(
            email = email,
            invalid = invalidEmail,
            onChangeEmail = { viewModel.updateEmail(it) })
        Spacer(modifier = Modifier.height(15.dp))
        PasswordInputLayout(
            password = password,
            invalid = invalidPassword,
            onChangePassword = { viewModel.updatePassword(it) })
        Spacer(modifier = Modifier.height(15.dp))
        NameInputLayout(name = name, onChangeSurname = {viewModel.updateName(it)})
        Spacer(modifier = Modifier.height(15.dp))
        SignUpButton() {
            if (isValidEmail(email.value) && isValidPassword(password.value)) {
                viewModel.onSignUpClick(onEvent)
            } else {
                if (!isValidEmail(email.value)) invalidEmail.value = true
                if (!isValidPassword(password.value)) invalidPassword.value = true
            }
        }
        Spacer(modifier = Modifier.height(15.dp))
        BackToSignInButton(onBack = onEvent)
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun SignUpButton (modifier:Modifier = Modifier,onRegistration:()->Unit){
    Button(
        modifier = modifier.fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Black),
        shape = RectangleShape,
        onClick = onRegistration
    ) {
        Text(text = stringResource(id = R.string.header_registration_screen))
    }
}

@Composable
fun BackToSignInButton(modifier:Modifier = Modifier, onBack:(SignUpEvent)->Unit){
    Button(
        modifier = modifier.fillMaxWidth(0.8f),
        border = BorderStroke(1.dp, Color.Blue),
        shape = RectangleShape,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.primary
        ),
        onClick = { onBack(SignUpEvent.BackToSignIpScreen) }
    ) {
        Text(text = "Назад")
    }
}

@Composable
fun NameInputLayout(name: State<String>, enabled: Boolean = true, onChangeSurname:(String)->Unit, modifier: Modifier = Modifier) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(0.8f),
        value = name.value,
        onValueChange = { onChangeSurname(it) },
        label = { if(enabled)Text(text = stringResource(id = R.string.text_input_name)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        enabled = enabled
    )
}