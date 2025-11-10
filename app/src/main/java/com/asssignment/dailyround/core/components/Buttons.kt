package com.asssignment.dailyround.core.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asssignment.dailyround.core.theme.DailyRoundAsssignmentTheme

@Composable
fun PrimaryButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        Text(text = buttonText)
    }
}

@Composable
fun SecondaryButton(
    buttonText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(text = buttonText)
    }
}

@Preview(showBackground = true, name = "Light Theme")
@Composable
fun ButtonPreviewLight() {
    DailyRoundAsssignmentTheme(darkTheme = false) {
        Column(modifier = Modifier.padding(16.dp)) {
            PrimaryButton(buttonText = "Start Quiz", onClick = {})
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(buttonText = "Try Again", onClick = {})
        }
    }
}

@Preview(showBackground = true, name = "Dark Theme", backgroundColor = 0xFF000000)
@Composable
fun ButtonPreviewDark() {
    DailyRoundAsssignmentTheme(darkTheme = true) {
        Column(modifier = Modifier.padding(16.dp)) {
            PrimaryButton(buttonText = "Start Quiz", onClick = {})
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(buttonText = "Try Again", onClick = {})
        }
    }
}
