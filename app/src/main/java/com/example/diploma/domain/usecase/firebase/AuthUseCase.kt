package com.example.diploma.domain.usecase.firebase

import com.example.diploma.domain.models.User
import com.example.diploma.domain.models.state.AuthResult
import com.example.diploma.domain.service.AccountService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthUseCase @Inject constructor(
    private val accountService: AccountService
) {
    val currentUser: Flow<User?> = accountService.currentUser
    val currentUserId: String = accountService.currentUserId
    fun hasUser(): Boolean = accountService.hasUser()
    suspend fun signIn(email: String, password: String): AuthResult =
        accountService.signIn(email = email, password = password)

    suspend fun signUp(email: String, password: String): AuthResult =
        accountService.signUp(email = email, password = password)

    suspend fun signOut() {
        accountService.signOut()
    }

    suspend fun deleteAccount() {
        accountService.deleteAccount()
    }

    fun updateEmail(email: String) {
        accountService.updateEmail(email)
    }

    fun updatePassword(password: String) {
        accountService.updatePassword(password)
    }

    fun getEmail(): String? = accountService.getEmail()

}