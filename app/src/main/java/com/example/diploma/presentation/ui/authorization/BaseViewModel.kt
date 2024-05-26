package com.example.diploma.presentation.ui.authorization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.diploma.domain.models.state.AuthResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    abstract val sendRequest: suspend (String, String) -> AuthResult

    private val _authState = MutableStateFlow<AuthResult>(AuthResult.Nothing)

    val authState: StateFlow<AuthResult> get() = _authState.asStateFlow()

    fun sendCredentials(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _authState.value =  AuthResult.Loading
            val result = sendRequest.invoke(email, password)
            _authState.value =  result
        }
    }
}