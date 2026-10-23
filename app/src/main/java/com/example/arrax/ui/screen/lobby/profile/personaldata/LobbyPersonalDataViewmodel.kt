package com.example.arrax.ui.screen.lobby.profile.personaldata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.arrax.core.common.AppError
import com.example.arrax.core.common.Resource
import com.example.arrax.domain.model.SignInMethod
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import com.example.arrax.domain.usecase.user.UpdateDisplayNameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LobbyPersonalDataViewmodel @Inject constructor(
    observeCurrentUser: ObserveCurrentUserUseCase,
    private val updateDisplayName: UpdateDisplayNameUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LobbyPersonalDataState())
    val uiState: StateFlow<LobbyPersonalDataState> = _uiState.asStateFlow()

    init {
        // Se precarga una sola vez para que las emisiones posteriores no pisen lo que se escribe.
        viewModelScope.launch {
            val user = observeCurrentUser().filterNotNull().first()
            _uiState.update {
                it.copy(
                    name = user.name,
                    originalName = user.name,
                    email = user.email,
                    isGoogleAccount = SignInMethod.GOOGLE in user.signInMethods,
                    isLoaded = true
                )
            }
        }
    }

    fun onNameChange(value: String) {
        _uiState.update {
            it.copy(
                name = value.take(UpdateDisplayNameUseCase.MAX_NAME_LENGTH),
                nameError = null,
                errorGeneral = null
            )
        }
    }

    /** onSaved navega atrás: el nombre ya se ve actualizado en el perfil (escritura local). */
    fun save(onSaved: () -> Unit) {
        val s = _uiState.value
        if (!s.canSave) return
        when (val r = updateDisplayName(s.name)) {
            is Resource.Success -> onSaved()
            is Resource.Error -> _uiState.update {
                if (r.throwable is AppError.Validation) it.copy(nameError = r.message)
                else it.copy(errorGeneral = r.message)
            }
            Resource.Loading -> Unit
        }
    }
}