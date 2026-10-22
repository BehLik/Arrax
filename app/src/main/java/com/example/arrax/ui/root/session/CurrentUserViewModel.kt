package com.example.arrax.ui.root.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.domain.model.User
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class CurrentUserViewModel @Inject constructor(
    observeCurrentUser: ObserveCurrentUserUseCase
) : ViewModel() {
    val user: StateFlow<User?> = observeCurrentUser()
        .catch { emit(null) }   // si el listener de Firestore falla, no tumbara la app....xd
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
}

