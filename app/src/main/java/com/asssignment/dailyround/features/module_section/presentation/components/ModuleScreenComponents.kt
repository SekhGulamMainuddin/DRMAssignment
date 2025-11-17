package com.asssignment.dailyround.features.module_section.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.asssignment.dailyround.core.components.AppTextBodyMedium
import com.asssignment.dailyround.core.components.AppTextBodySmall
import com.asssignment.dailyround.core.components.AppTextLabelMedium
import com.asssignment.dailyround.features.module_section.domain.ModuleModel
import com.asssignment.dailyround.features.module_section.domain.ModuleStatus
import com.asssignment.dailyround.features.module_section.presentation.viewmodel.ModuleUIState
import com.asssignment.dailyround.features.quiz.presentation.components.MessageAndOrRetry

@Composable
fun ModuleScreenContent(
    state: ModuleUIState,
    modifier: Modifier = Modifier,
    onModuleClick: (ModuleModel) -> Unit,
    onRetryTap: () -> Unit,
) {
    when (state) {
        ModuleUIState.Loading -> {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ModuleUIState.ErrorState -> {
            Box(
                modifier = modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                MessageAndOrRetry(message = state.message, onClickRetry = onRetryTap)
            }
        }

        is ModuleUIState.LoadedState -> {
            ModuleList(
                modifier = modifier,
                modules = state.list,
                onModuleClick = onModuleClick
            )
        }
    }
}

@Composable
fun ModuleList(
    modifier: Modifier,
    modules: List<ModuleModel>,
    onModuleClick: (ModuleModel) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(modules) { module ->
            ModuleTile(module = module, onClick = { onModuleClick(module) })
        }
    }
}


@Composable
fun ModuleTile(
    module: ModuleModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            // Title
            AppTextLabelMedium(
                text = module.title,
            )

            // Description
            AppTextBodyMedium(
                text = module.description,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Show progress only if completed
            if (module.status == ModuleStatus.REVIEW) {
                AppTextBodySmall(
                    text = "${module.totalQuestions} Questions | Score: ${module.correctAnswers}/10",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            // Status Button Row
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                ModuleStatusButton(module.status)
            }
        }
    }
}



@Composable
fun ModuleStatusButton(status: ModuleStatus) {
    val label = when (status) {
        ModuleStatus.START -> "Start"
        ModuleStatus.CONTINUE -> "Continue"
        ModuleStatus.REVIEW -> "Review"
    }

    val textColor = when (status) {
        ModuleStatus.START -> MaterialTheme.colorScheme.primary
        ModuleStatus.CONTINUE -> MaterialTheme.colorScheme.secondary
        ModuleStatus.REVIEW -> MaterialTheme.colorScheme.tertiary
    }

    AppTextBodyMedium(
        label,
        color = textColor
    )
}
