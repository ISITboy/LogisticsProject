package com.example.diploma.presentation.ui.authorization.signin

sealed class SignInEvent {
    data class GoToSignUpScreen(val email:String):SignInEvent()
    data object SignIn:SignInEvent()
}