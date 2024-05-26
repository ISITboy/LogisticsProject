package com.example.diploma.data.impl.service


import com.example.diploma.domain.models.User
import com.example.diploma.domain.models.state.AuthResult
import com.example.diploma.domain.service.AccountService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor (): AccountService {
    override val currentUser: Flow<User?>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User.Base(id = it.uid, email = it.email?:"") })
                }
            Firebase.auth.addAuthStateListener(listener)
            awaitClose { Firebase.auth.removeAuthStateListener(listener) }
        }

    override val currentUserId: String
        get() = Firebase.auth.currentUser?.uid.orEmpty()

    override fun hasUser(): Boolean {
        return Firebase.auth.currentUser != null
    }

    override fun updateEmail(email:String){
        FirebaseAuth.getInstance().currentUser?.updateEmail(email)
    }

    override fun updatePassword(password:String){
        FirebaseAuth.getInstance().currentUser?.updatePassword(password)
    }
    override fun getEmail() = Firebase.auth.currentUser?.email

    override suspend fun signIn(email: String, password: String) : AuthResult {
        return try {
            val user = Firebase.auth.signInWithEmailAndPassword(email, password).await().user!!
            AuthResult.Success(User.Base(id = user.uid,email =user.email ?: " "))
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }

    override suspend fun signUp(email: String, password: String): AuthResult {
        return try {
            val user =  Firebase.auth.createUserWithEmailAndPassword(email, password).await().user!!
            AuthResult.Success(User.Base(id = user.uid,email =user.email ?: " "))
        } catch (e: Exception) {
            AuthResult.Error(e)
        }
    }

    override suspend fun signOut() {
        Firebase.auth.signOut()
    }

    override suspend fun deleteAccount() {
        Firebase.auth.currentUser!!.delete().await()
    }
}