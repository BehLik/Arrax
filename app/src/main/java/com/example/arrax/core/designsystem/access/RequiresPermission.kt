package com.example.arrax.core.designsystem.access

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.arrax.domain.model.Access
import com.example.arrax.domain.model.Permission
import com.example.arrax.ui.screen.business.access.BusinessAccessViewModel


val LocalAccess = staticCompositionLocalOf<Access> { Access.Loading }
enum class Fallback { Hide, Disable }

@Composable
fun ProvideAccess(
    onRevoked: () -> Unit,
    required: Permission? = null,
    viewModel: BusinessAccessViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val access by viewModel.access.collectAsStateWithLifecycle()

    val allowed = when (val a = access) {
        Access.Loading -> null                       // aún no se sabe
        Access.None -> false
        is Access.Granted -> required == null || a.can(required)
    }
    LaunchedEffect(allowed) { if (allowed == false) onRevoked() }

    CompositionLocalProvider(LocalAccess provides access) {
        if (allowed == true) content()               // no se dibuja nada protegido antes de saber
        // con Loading puedes mostrar ArxLoading() aquí
    }
}

@Composable
fun RequiresPermission(
    permission: Permission,
    fallback: Fallback = Fallback.Hide,
    content: @Composable (enabled: Boolean) -> Unit
) {
    val access = LocalAccess.current
    val allowed = access is Access.Granted && access.can(permission)
    when {
        allowed -> content(true)
        fallback == Fallback.Disable -> content(false)
        else -> Unit
    }
}