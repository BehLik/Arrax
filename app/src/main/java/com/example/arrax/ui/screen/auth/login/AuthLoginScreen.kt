package com.example.arrax.ui.screen.auth.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.ui.screen.auth.login.component.LoginActions
import com.example.arrax.ui.screen.auth.login.component.LoginForm
import com.example.arrax.ui.screen.auth.login.component.LoginHeader


//@Composable
//fun AuthLoginScreen(
//    viewModel: LoginViewModel = hiltViewModel(),
//    onNavigateToHome: () -> Unit,
//    onNavigateToRegister: () -> Unit,
//    onNavigateToForgotPassword: () -> Unit
//) {
//    val uiState by viewModel.uiState.collectAsState()
//
//    ArxBackground {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(horizontal = 24.dp),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
//        ) {
//            // 1. SEPARACIÓN: ENCABEZADO
//            LoginHeader()
//
//            Spacer(modifier = Modifier.height(40.dp))
//
//            // 2. SEPARACIÓN: FORMULARIO (Campos de entrada)
//            LoginForm(
//                email = uiState.email,
//                password = uiState.password,
//                errorMessage = uiState.errorMessage,
//                onEmailChange = viewModel::onEmailChange,
//                onPasswordChange = viewModel::onPasswordChange
//            )
//
//            Spacer(modifier = Modifier.height(32.dp))
//
//            // 3. SEPARACIÓN: ACCIONES (Botones y Navegación)
//            LoginActions(
//                isLoading = uiState.isLoading,
//                isButtonEnabled = uiState.email.isNotEmpty() && uiState.password.isNotEmpty(),
//                onLoginClick = { viewModel.login(onNavigateToHome) },
//                onRegisterClick = onNavigateToRegister,
//                onForgotClick = onNavigateToForgotPassword
//            )
//        }
//    }
//}