package com.example.arrax.ui.screen.business.event.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.icons.ArxIconBadge
import com.example.arrax.core.designsystem.icons.ArxIconShape
import com.example.arrax.core.designsystem.icons.ArxIconStyle
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.CutState
import com.example.arrax.domain.model.CutCatalog



@Composable
fun CutSelectCard(cut: CutCatalog, selected: Boolean, onClick: () -> Unit) {
    val colors = MaterialTheme.colorScheme
    val shape = MaterialTheme.shapes.medium
    Box(
        modifier = Modifier.then(
            if (selected) Modifier.border(2.dp, colors.primary, shape) else Modifier
        )
    ) {
        ArxCard(onClick = onClick) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(ArxSpacing.sm),
                verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                CutImage(cut, Modifier.fillMaxWidth().aspectRatio(1f).clip(shape))
                Text(cut.name, style = MaterialTheme.typography.titleSmall, maxLines = 1)
                Text(
                    text = if (cut.state == CutState.COOKED) "Cocido" else "Fresco",
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.onSurfaceVariant
                )
            }
        }
        if (selected) {
            ArxIconBadge(
                icon = ArxIcons.Check,
                style = ArxIconStyle.Solid,
                shape = ArxIconShape.Circle,
                containerSize = 24.dp,
                iconSize = 16.dp,
                modifier = Modifier.align(Alignment.TopEnd).padding(ArxSpacing.sm)
            )
        }
    }
}