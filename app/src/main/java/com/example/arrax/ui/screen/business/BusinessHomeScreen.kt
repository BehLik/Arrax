package com.example.arrax.ui.screen.business

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.ui.screen.events.create.EventCreateViewModel

@Composable
fun BusinessHomeScreen(
    onBack: () -> Unit,
    onCreateEventClick: ()-> Unit,
    onEventClick:()-> Unit,
    viewModel: BusinessHomeViewModel = hiltViewModel()
){}