package com.example.arrax.ui.screen.business.more.subscreen.members.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.example.arrax.core.designsystem.theme.ColorFamily
import com.example.arrax.core.designsystem.theme.extendedColors
import com.example.arrax.domain.model.Role

@Composable
internal fun Role.chipFamily(): ColorFamily {
    val ext = MaterialTheme.extendedColors
    return when (this) {
        Role.OWNER -> ext.roleOwner
        Role.ADMIN -> ext.roleAdmin
        else -> ext.roleOperator
    }
}