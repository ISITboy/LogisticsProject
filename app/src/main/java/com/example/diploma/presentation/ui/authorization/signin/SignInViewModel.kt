package com.example.diploma.presentation.ui.authorization.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diploma.domain.models.state.AuthResult
import com.example.diploma.domain.usecase.firebase.AuthUseCase
import com.example.diploma.presentation.mainActivity.NavigationState
import com.example.diploma.presentation.ui.authorization.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authUseCase: AuthUseCase
):BaseViewModel() {
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun onSignInClick(onEvent: (SignInEvent) -> Unit) {
        sendCredentials(email.value,password.value)
        viewModelScope.launch {
            authState.collect{
                if(it is AuthResult.Success)onEvent(SignInEvent.SignIn)
            }
        }
    }

    fun onSignUpClick(onEvent: (SignInEvent) -> Unit) {
        val email = this.email.value.ifBlank { "empty" }
        onEvent(SignInEvent.GoToSignUpScreen(email))
    }

    override val sendRequest: suspend (String, String) -> AuthResult =
        { email, password -> authUseCase.signIn(email, password) }

}