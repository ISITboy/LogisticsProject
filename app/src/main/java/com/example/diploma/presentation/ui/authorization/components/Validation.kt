package com.example.applicationquizforstudents.presentation.ui.screens.authorization.components

fun isValidEmail(email: String): Boolean {
    val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
    return emailRegex.matches(email)
}

fun isValidPassword(password: String): Boolean {
    val minLength = 8
    return password.length >= minLength
}