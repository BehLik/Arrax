package com.example.arrax.ui.screen.business.home

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.arrax.ui.screen.business.home.BusinessHomeViewModel

@Composable
fun BusinessHomeScreen(
    onBack: () -> Unit,
    onCreateEventClick: ()-> Unit,
    onEventClick:()-> Unit,
    viewModel: BusinessHomeViewModel = hiltViewModel()
){}
