package com.example.diploma.domain.models.state

import com.example.diploma.domain.models.User


sealed class AuthResult {

    class Success(val user: User) : AuthResult()

    class Error(val e: Exception) : AuthResult()

    object Loading : AuthResult()

    object Nothing : AuthResult()
}