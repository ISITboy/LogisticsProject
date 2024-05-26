package com.example.diploma.domain.service

import com.example.diploma.domain.models.state.AuthResult
import com.example.diploma.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUser: Flow<User?>
    val currentUserId: String
    fun hasUser(): Boolean
    suspend fun signIn(email: String, password: String): AuthResult
    suspend fun signUp(email: String, password: String): AuthResult
    suspend fun signOut()
    suspend fun deleteAccount()

    fun updateEmail(email:String)
    fun updatePassword(password:String)
    fun getEmail():String?
}