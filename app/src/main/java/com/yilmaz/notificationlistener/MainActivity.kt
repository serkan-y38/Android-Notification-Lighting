package com.yilmaz.notificationlistener

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.yilmaz.notificationlistener.ui.theme.NotificationListenerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotificationListenerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .padding(20.dp)
                                .align(Alignment.Center)
                        ) {
                            Image(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(100.dp)
                                    .align(Alignment.CenterHorizontally),
                                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                                contentDescription = ""
                            )
                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight()
                                    .padding(vertical = 20.dp),
                                style = MaterialTheme.typography.titleLarge,
                                text = "Please Allow 'Notification Access' and 'Display Over other apps'"
                            )
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentHeight(),
                                onClick = {
                                    drawOverlayPermissionSetting()
                                    notificationServicePermission()
                                },
                                content = {
                                    Text(text = "Enable - Disable")
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun notificationServicePermission() {
        applicationContext.startActivity(
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
    }

    private fun drawOverlayPermissionSetting() {
        applicationContext.startActivity(
            Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            ).addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
            )
        )
    }

    // private fun checkDrawOverlayPermission(): Boolean = Settings.canDrawOverlays(this)

}