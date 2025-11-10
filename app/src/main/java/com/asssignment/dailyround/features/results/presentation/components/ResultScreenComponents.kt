package com.asssignment.dailyround.features.results.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.asssignment.dailyround.core.components.AppTextBodyLarge
import com.asssignment.dailyround.core.components.AppTextTitleMedium
import com.asssignment.dailyround.core.theme.DailyRoundAsssignmentTheme

@Composable
fun SummaryComponent(modifier: Modifier = Modifier, title: String, value: String) {
    Card(
        modifier = modifier
            .height(150.dp)
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground,
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
    ) {
        // Add a Column with padding for the inner content
        Column(
            modifier = Modifier
                .padding(16.dp) // inner padding inside the Card
        ) {
            AppTextTitleMedium(
                modifier = Modifier.padding(bottom = 10.dp),
                text = title,
                color = MaterialTheme.colorScheme.onBackground
            )
            AppTextBodyLarge(
                text = value,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}


@Preview
@Composable
private fun SummaryComponentSummary() {
    DailyRoundAsssignmentTheme {
        SummaryComponent(title = "Correct Answers", value = "8/10")
    }
}