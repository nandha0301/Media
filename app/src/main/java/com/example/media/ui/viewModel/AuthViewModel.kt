package com.example.media.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.media.ui.repository.AuthRepository
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject




@HiltViewModel
class AuthViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    var isLoggedIn = false
        private set

    var isLoading by mutableStateOf(false)

    fun signUp(name: String, email: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            isLoading = true // Start loading
            val result = authRepository.signUp(name, email, password)
            isLoading = false // Stop loading
            onResult(result)
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val (success, errorMessage) = authRepository.login(email, password)
            isLoggedIn = success
            onResult(success, errorMessage)
        }
    }

    fun getUserDetails(onResult: (DocumentSnapshot?) -> Unit) {
        val userId = authRepository.getCurrentUser()?.uid
        if (userId != null) {
            viewModelScope.launch {
                val userDetails = authRepository.getUserDetails(userId)
                onResult(userDetails)
            }
        } else {
            onResult(null)
        }
    }

    fun logout() {
        authRepository.logout()
        isLoggedIn = false
    }

    fun getCurrentUser() = authRepository.getCurrentUser()
}