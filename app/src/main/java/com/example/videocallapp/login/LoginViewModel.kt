package com.example.videocallapp.login

import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Normal)
    val loginStateFlow = _loginState.asStateFlow()
    val loginState: StateFlow<LoginState> = _loginState

    fun login(username: String, password: String) {
        val auth = Firebase.auth
        _loginState.value = LoginState.Loading
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        _loginState.value = LoginState.Success
                    } else {
                        _loginState.value = LoginState.Failed
                    }
                } else {
                    _loginState.value = LoginState.Failed
                }
            }
    }
}

sealed class LoginState {
    object Normal : LoginState()
    object Success : LoginState()
    object Failed : LoginState()
    object Loading : LoginState()
}
