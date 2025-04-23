package me.auth_android.auth_kit.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import me.auth_android.auth_kit.presentation.utils.LocalWindowClass

@Composable
internal fun AdaptiveView(content: @Composable () -> Unit) {
    val windowClass = LocalWindowClass.getLocalWindowClass()
    if (windowClass == LocalWindowClass.WindowClass.COMPACT) {
        CompactView(content)
    } else {
        MediumOrExpandView(content)
    }
}

@Composable
private fun CompactView(content: @Composable () -> Unit) {
    Scaffold { padding -> Box(modifier = Modifier.fillMaxSize().padding(padding)) { content() } }
}

@Composable
private fun MediumOrExpandView(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier.height(600.dp).width(450.dp).clip(shape = RoundedCornerShape(16.dp))
        ) {
            content()
        }
    }
}
