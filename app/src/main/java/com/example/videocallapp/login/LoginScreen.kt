package com.example.videocallapp.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun LoginScreen(navController: NavController) {
    val viewModel: LoginViewModel = hiltViewModel()
    val username = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val loading = remember { mutableStateOf(false) }
    Text("Login")
    Column (modifier = Modifier.fillMaxSize().padding(16.dp),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = username.value,
            onValueChange = { username.value = it },
            label = { Text(text ="Username") }
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(text ="Password") }
        )
        if (loading.value) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { viewModel.login(username.value, password.value) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Login")
            }
            TextButton(onClick = {
                navController.navigate("signup")
            }) { Text("Don't have an account? Sign up") }
        }
    }
    val state = viewModel.loginStateFlow.collectAsState()
    LaunchedEffect(state.value) {
    when(state.value){
        is LoginState.Normal -> {
            loading.value = false
        }
        is LoginState.Success -> {
            navController.navigate("home"){
                popUpTo("login"){
                    inclusive = true
                }
            }
        }
        is LoginState.Loading -> {
            loading.value = true
        }
        is LoginState.Failed -> {
            loading.value = false
        }
    }
}
    }


@Composable
@Preview(showBackground = true)
fun LoginScreenPreview(modifier: Modifier = Modifier) {
    LoginScreen(rememberNavController())
}