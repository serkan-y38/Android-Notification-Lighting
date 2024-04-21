package com.yilmaz.notificationlistener

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.yilmaz.notificationlistener.ui.theme.NotificationListenerTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowInsetsControllerCompat(window, window.decorView.rootView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.navigationBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }

        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        setContent {

            val context = LocalContext.current
            val dataStoreUtils = DataStoreUtils(context)
            val coroutineScope = rememberCoroutineScope()
            val size = DataStoreUtils(applicationContext).getScreenCornerRadiusSize()
                .collectAsState(initial = 32)
            var sliderPosition by remember { mutableFloatStateOf(size.value.toFloat()) }

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
                                .background(Color.Black),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            AnimatedBorder(
                                shape = RoundedCornerShape(size = size.value.dp),
                                gradientColors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    Color.Black,
                                    MaterialTheme.colorScheme.primary,
                                    Color.Black
                                ),
                                icon = null,
                                content = {
                                    Column(
                                        modifier = Modifier
                                            .padding(48.dp)
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
                                        Text(
                                            textAlign = TextAlign.Center,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .wrapContentHeight()
                                                .padding(top = 40.dp),
                                            style = MaterialTheme.typography.titleMedium,
                                            text = "Set screen corner radius size"
                                        )
                                        Slider(
                                            value = sliderPosition,
                                            onValueChange = {
                                                sliderPosition = it
                                                coroutineScope.launch {
                                                    dataStoreUtils.setScreenCornerRadiusSize(
                                                        sliderPosition.toInt()
                                                    )
                                                }
                                            },
                                            colors = SliderDefaults.colors(
                                                thumbColor = MaterialTheme.colorScheme.secondary,
                                                activeTrackColor = MaterialTheme.colorScheme.secondary,
                                                inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
                                            ),
                                            valueRange = 16f..64f
                                        )
                                        Text(text = sliderPosition.toInt().toString())
                                    }

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