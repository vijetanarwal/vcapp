package com.example.videocallapp.signup



import androidx.lifecycle.ViewModel
import com.example.videocallapp.login.LoginState
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _loginState = MutableStateFlow<SignUpState>(SignUpState.Normal)
    val signUpState = _loginState.asStateFlow()
    val signupState: StateFlow<SignUpState> = _loginState

    fun signUp(username: String, password: String) {
        val auth = Firebase.auth
        _loginState.value = SignUpState.Loading
        auth.createUserWithEmailAndPassword(username, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        _loginState.value = SignUpState.Success
                    } else {
                        _loginState.value = SignUpState.Failed
                    }
                } else {
                    _loginState.value = SignUpState.Failed
                }
            }
    }
}

sealed class SignUpState {
    object Normal : SignUpState()
    object Success : SignUpState()
    object Failed : SignUpState()
    object Loading : SignUpState()
}
