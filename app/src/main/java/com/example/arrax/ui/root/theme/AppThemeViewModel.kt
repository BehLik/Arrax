package com.example.arrax.ui.root.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.model.ThemeMode
import com.example.arrax.domain.usecase.user.ObservePreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppThemeViewModel @Inject constructor(
    observePreferences: ObservePreferencesUseCase
) : ViewModel() {
    val themeMode: StateFlow<ThemeMode> = observePreferences()
        .map { it.themeMode }
        .distinctUntilChanged()
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeMode.SYSTEM)
}