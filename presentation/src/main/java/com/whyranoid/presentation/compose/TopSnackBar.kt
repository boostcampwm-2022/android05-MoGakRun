package com.whyranoid.presentation.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.whyranoid.presentation.R

@Composable
fun TopSnackBar(isVisible: Boolean, text: String) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutVertically(
            targetOffsetY = { fullHeight -> -fullHeight },
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(color = colorResource(id = R.color.mogakrun_secondary_dark)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.padding(8.dp),
                text = text,
                color = colorResource(id = R.color.mogakrun_on_secondary)
            )
        }
    }
}
