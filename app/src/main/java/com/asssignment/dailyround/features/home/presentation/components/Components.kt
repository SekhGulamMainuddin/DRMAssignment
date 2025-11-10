package com.asssignment.dailyround.features.home.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.asssignment.dailyround.R
import com.asssignment.dailyround.core.components.AppTextLabelLarge

@Composable
fun HomeLottieAnim(modifier: Modifier = Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.quiz_home_anim))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        modifier = modifier.scale(1.0f),
        composition = composition,
        progress = { progress },
    )
}

@Composable
fun HighlightsBox(modifier: Modifier = Modifier, color: Color, value: String) {
    Box(
        modifier = modifier
            .border(1.5.dp, color = color, shape = RoundedCornerShape(8.dp))
            .padding(16.dp)
    ) {
        AppTextLabelLarge(text = value)
    }
}
