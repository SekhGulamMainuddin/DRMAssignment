package com.asssignment.dailyround.features.module_section.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.asssignment.dailyround.R
import com.asssignment.dailyround.core.components.AppTextTitleMedium
import com.asssignment.dailyround.core.navigation.NavigationHelper
import com.asssignment.dailyround.features.module_section.presentation.components.ModuleScreenContent
import com.asssignment.dailyround.features.module_section.presentation.viewmodel.ModuleViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModuleScreen(navController: NavController) {
    val viewModel = hiltViewModel<ModuleViewModel>()
    val uiState by viewModel.moduleUIState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadModules()
    }

    SideEffect {
        viewModel.refreshModules()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { AppTextTitleMedium(stringResource(R.string.modules)) }
            )
        }
    ) { paddingValues ->
        ModuleScreenContent(
            state = uiState,
            modifier = Modifier.padding(paddingValues),
            onModuleClick = { module ->
                navController.navigate(NavigationHelper.HomeRoute.createRoute(moduleId = module.id, questionUrl = module.questionsUrl))
            },
            onRetryTap = {
                viewModel.loadModules()
            }
        )
    }
}
