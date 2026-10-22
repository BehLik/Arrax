package com.example.arrax.ui.root.session

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.example.arrax.core.designsystem.components.display.ArxAvatar
import com.example.arrax.core.designsystem.components.display.arxInitials
import com.example.arrax.domain.model.User

val LocalCurrentUser = compositionLocalOf<User?> { null }

@Composable
fun CurrentUserAvatar(size: Dp, modifier: Modifier = Modifier) {
    val user = LocalCurrentUser.current
    ArxAvatar(
        initials = arxInitials(user?.name.orEmpty().ifBlank { user?.email.orEmpty() }.ifBlank { "?" }),
        imageUrl = user?.photoUrl,
        size = size,
        modifier = modifier
    )
}