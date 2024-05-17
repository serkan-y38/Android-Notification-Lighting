package com.yilmaz.notificationlistener

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.palette.graphics.Palette
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import com.yilmaz.notificationlistener.ui.theme.NotificationListenerTheme

class WakeupActivity : ComponentActivity() {

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

        setShowWhenLocked(true)
        setTurnScreenOn(true)

        val packageName = intent.getStringExtra(NotificationListenerService.PACKAGE_NAME)
        val icon = packageManager.getApplicationIcon(packageName!!)
        val color1 = getVibrantColor(icon.toBitmap())
        val color2 = Color.Black

        setContent {
            val cornerRadiusSize = DataStoreUtils(
                applicationContext
            ).getScreenCornerRadiusSize().collectAsState(initial = 32)

            val borderSize = DataStoreUtils(
                applicationContext
            ).getBorderSize().collectAsState(initial = 4)

            val animationFrequency = DataStoreUtils(
                applicationContext
            ).getAnimationFrequency().collectAsState(initial = 1)

            val colorList = mutableListOf<Color>()
            for (i in 1..animationFrequency.value) {
                colorList.add(color1)
                colorList.add(color2)
            }

            NotificationListenerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedBorder(
                            shape = RoundedCornerShape(size = cornerRadiusSize.value.dp),
                            gradientColors = colorList,
                            icon = icon,
                            content = {},
                            borderSize = borderSize.value.dp
                        )
                    }
                    Handler(Looper.getMainLooper()).postDelayed({ finish() }, 5000)
                }
            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun getVibrantColor(bm: Bitmap): Color =
        Color(Palette.from(bm).generate().getVibrantColor(R.color.white))
}

@Composable
fun AnimatedBorder(
    icon: Drawable?,
    gradientColors: List<Color>,
    shape: Shape,
    content: @Composable() () -> Unit,
    borderSize: Dp
) {
    val infiniteTransaction = rememberInfiniteTransition(label = "infinite color transaction")

    val degrees by infiniteTransaction.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        label = "infinite colors",
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 5000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        )
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = shape,
            content = {
                Surface(
                    Modifier
                        .fillMaxSize()
                        .padding(borderSize)
                        .drawWithContent {
                            rotate(degrees = degrees) {
                                drawCircle(
                                    brush = Brush.sweepGradient(gradientColors),
                                    radius = 2000f,
                                    blendMode = BlendMode.SrcIn
                                )
                            }
                            drawContent()
                        },
                    color = Color.Black,
                    shape = shape,
                    content = { content() }
                )
            }
        )
        Image(
            modifier = Modifier
                .width(64.dp)
                .height(64.dp)
                .align(Alignment.Center),
            painter = rememberDrawablePainter(drawable = icon),
            contentDescription = "app icon"
        )
    }
}