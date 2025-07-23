package com.example.videocallapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.videocallapp.home.HomeScreen
import com.example.videocallapp.login.LoginScreen
import com.example.videocallapp.login.SignUpScreen
import com.example.videocallapp.ui.theme.VideoCallAppTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VideoCallAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (modifier = Modifier.padding(innerPadding)){
                        NavHostScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun NavHostScreen(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    var start = "login"
    FirebaseAuth.getInstance().currentUser?.let { start = "home"  }
    NavHost(navController = navController, startDestination = start){
        composable("login"){
            LoginScreen(navController)
        }
        composable("home"){
            SignUpScreen(navController)
        }
        composable("signup"){
            HomeScreen(navController)
        }
    }
}
