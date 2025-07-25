package com.example.videocallapp.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.zegocloud.uikit.prebuilt.call.invite.ZegoSendCallInvitationButton

@Composable
fun HomeScreen(navController: NavController) {
    val context = LocalContext.current as MainActivity
    LaunchedEffect = Unit {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let{
            context.initZegoInviteService(appID,appSign,it.email!!4,it.email!!)
        }

    }
    Column (modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        val targetUserId = remember { mutableStateOf("") }
        Text("Video Call App")
        OutlinedTextField(value = targetUserId.value,
            onValueChange = {targetUserId.value = it},
            label = { Text(text ="Add user email") },
            modifier = Modifier.fillMaxWidth()
        )
        Row{
            CallButton(isVideoCall = false){
                button -> if(targetUserId.value.isNotEmpty())
                    button.sendInvitees(mutableListOf(ZegoUIKitUser{targetUserId.value,targetUserId.value}))
            }
            CallButton(isVideoCall = true){
                button -> if(targetUserId.value.isNotEmpty())   button.sendInvitees(
                mutableListOf(
                    ZegoUIKitUser(
                        targetUserId.value, targetUserId.value
                    )
                )
            )
            }
        }
    }
}

@Composable
fun CallButton(isVideoCall:Boolean,onClick(ZegoSendCallInvitationButton)->Unit){
    AndroidView(factory = { context ->
        val button = ZegoSendCallInvitationButton(context)
        button.setIsVideoCall(isVideoCall)
        button.resourceID = "zego_data"
        button
    }){
        zegoCallButton ->
        zegoCallButton.setOn
    }
}