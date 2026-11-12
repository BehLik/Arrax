
package com.example.arrax.ui.screen.events.home.subscreen.edit.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.cards.ArxCard
import com.example.arrax.core.designsystem.components.inputs.ArxTextField
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.ui.screen.business.event.component.CutImage
import com.example.arrax.ui.screen.events.home.subscreen.edit.EditableCut

@Composable
internal fun EventEditCutRow(
    cut: EditableCut,
    catalogCut: CutCatalog?,
    focusRequester: FocusRequester,
    isLast: Boolean,
    onPriceChange: (String) -> Unit,
    onNext: () -> Unit,
    onDone: () -> Unit,
    onRemove: () -> Unit
) {
    ArxCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(ArxSpacing.md),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.md)
        ) {
            if (catalogCut != null) {
                CutImage(
                    catalogCut,
                    Modifier
                        .size(56.dp)
                        .clip(MaterialTheme.shapes.small)
                )
            } else {
                Spacer(Modifier.size(56.dp))
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cut.name,
                    style = MaterialTheme.typography.titleSmall
                )

                ArxTextField(
                    value = cut.priceText,
                    onValueChange = onPriceChange,
                    label = "Precio por kg ($)",
                    modifier = Modifier.focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal,
                        imeAction = if (isLast) {
                            ImeAction.Done
                        } else {
                            ImeAction.Next
                        }
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { onNext() },
                        onDone = { onDone() }
                    )
                )
            }

            IconButton(
                onClick = onRemove
            ) {
                ArxIcon(
                    icon = ArxIcons.Delete,
                    tint = MaterialTheme.colorScheme.error,
                    size = 20.dp
                )
            }
        }
    }
}
