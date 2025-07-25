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
class MainActivity : FragmentActivity() {
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
        permissionHandling(this)
    }
}

fun initZegoInviteService(appId:Long,appSign:String,userId:String,userName:String){
    val callInvitationConfig = ZegoKitPrebuiltCallInvitationConfig()
    callInvitationConfig.translationText = ZegoTranslationText(ZegoUiKitLanguage.ENGLISH)
    callInvitationConfig.provider = LegoKitPrebuiltCallConfigProvider {invitationData : ZegoCallInvitationData? ->
        ZegoUIKitPrebuiltCallInvitationConfig.generateDefaultCoonfig(
            invitationData
        )
    }
    ZegoUIKitPrebuiltCallService.events.errorEventsListener =
        ErrorEventsListener{ errorCode: Int,meesage:String ->
            Timber.d("onError() called with: errorCode = [$errorCode], message = [$message]")
        }
    ZegoUIKitPrebuiltCallService.events.invitationEvents.pluginConnectListener =
        signalPluginConnectListener{ state: ZIMConnectionState, event: ZIMConnectionEvent, extendedData: JSONObject ->
            Timber.d("onSignalPluginConnectionStateChanged() called with: state = [$state],event = [$event],extendedData = [$extendedData$]")
        }
    ZegoUIKitPrebuiltCallService.init(
        application,appID,appSign,userID,userName,callInvitationConfig
    )
    ZegouIKitPrebuiltCallService.events.callEvents.callEndListener =
        callEndListener{ callEndReason, jsonObject ->}{ callEndReason: ZegoCallEndReason, jsonObject: String ->
            Timber.d("onCallEnd() called with: callEndReason = [$callEndReason] , jsonObject = [$jsonObject]")
        }
}
override fun onDestroy() {
    super.onDestroy()
    ZegoUIKitPrebuiltCallService.unInit()
}
private fun permissionHandling(activityContext : FragmentActivity){
    PermissionX.init(activityContext).permissions(permission.SYSTEM_ALERT_WINDOW)
        .onExplanationRequestReason{ scope, deniedList ->
            val message = "We need permission to call you"
            scope.showRequestReasonDialog(deniedList,"PermissionX",message,"Allow","Deny")
        }.request{ allGranted,grantedList,deniedList ->}
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
