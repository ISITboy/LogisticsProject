package com.example.diploma.presentation.ui.authorization.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.diploma.domain.models.User
import com.example.diploma.domain.models.state.AuthResult
import com.example.diploma.domain.service.RealtimeService
import com.example.diploma.domain.usecase.firebase.AuthUseCase
import com.example.diploma.presentation.ui.authorization.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val realtimeService: RealtimeService
): BaseViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password.asStateFlow()

    private val _name = MutableStateFlow("")
    val name : StateFlow<String> = _name.asStateFlow()


    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun updateName(newEmail: String) {
        _name.value = newEmail
    }


    fun getState() = realtimeService.getState().asLiveData()

    fun createUser(user: User.Base) = viewModelScope.launch {
        realtimeService.createUser(user)
    }


    fun onSignUpClick(event:(SignUpEvent)->Unit ) {
        sendCredentials(email.value, password.value)
        viewModelScope.launch {
            authState.collect{
                if(it is AuthResult.Success) {
                    event(SignUpEvent.SignUp)
                }
            }
        }
    }

    override val sendRequest: suspend (String, String) -> AuthResult =
        { email, password -> authUseCase.signUp(email, password) }
}