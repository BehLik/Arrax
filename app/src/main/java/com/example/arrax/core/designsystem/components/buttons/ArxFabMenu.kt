package com.example.arrax.core.designsystem.components.buttons

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing

data class ArxFabMenuItem(
    val text: String,
    val iconRes: Int,
    val onClick: () -> Unit
)

/** FAB que despliega acciones relacionadas hacia arriba. Atrás lo contrae. */
@Composable
fun ArxFabMenu(
    items: List<ArxFabMenuItem>,
    modifier: Modifier = Modifier
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    BackHandler(enabled = expanded) { expanded = false }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
    ) {
        AnimatedVisibility(
            visible = expanded,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
        ) {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                items.forEach { item ->
                    ArxExtendedFAB(
                        text = item.text,
                        icon = painterResource(item.iconRes),
                        onClick = {
                            expanded = false
                            item.onClick()
                        }
                    )
                }
            }
        }

        ArxFab(
            icon = painterResource(if (expanded) ArxIcons.Close else ArxIcons.Add),
            contentDescription = if (expanded) "Cerrar menú" else "Abrir menú",
            onClick = { expanded = !expanded }
        )
    }
}