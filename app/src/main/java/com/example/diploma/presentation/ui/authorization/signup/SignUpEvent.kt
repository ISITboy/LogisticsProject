package com.example.diploma.presentation.ui.authorization.signup

sealed class SignUpEvent {
    data object BackToSignIpScreen:SignUpEvent()
    data object SignUp:SignUpEvent()
}