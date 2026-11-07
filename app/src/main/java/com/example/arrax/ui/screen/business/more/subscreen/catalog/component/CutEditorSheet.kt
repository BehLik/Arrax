package com.example.arrax.ui.screen.business.more.subscreen.catalog.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.CutState
import com.example.arrax.domain.model.CutOrigin
import com.example.arrax.ui.screen.business.more.subscreen.catalog.CutEditorState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CutEditorSheet(
    editor: CutEditorState,
    onChange: ((CutEditorState) -> CutEditorState) -> Unit,
    onSave: () -> Unit,
    onDismiss: () -> Unit,
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = ArxSpacing.md)
                .navigationBarsPadding()
                .imePadding()
                .padding(bottom = ArxSpacing.md),
            verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
        ) {
            Text(
                if (editor.isNew) "Nuevo corte" else "Editar corte",
                style = MaterialTheme.typography.titleLarge,
            )
            if (editor.origin == CutOrigin.DEFAULT || editor.origin == CutOrigin.MODIFIED) {
                Text(
                    "Este corte viene del sistema. Si lo cambias, tu negocio usará tu versión.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            OutlinedTextField(
                value = editor.name,
                onValueChange = { v -> onChange { it.copy(name = v, nameError = null) } },
                label = { Text("Nombre") },
                isError = editor.nameError != null,
                supportingText = editor.nameError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            OutlinedTextField(
                value = editor.description,
                onValueChange = { v -> onChange { it.copy(description = v) } },
                label = { Text("Descripción (opcional)") },
                minLines = 2,
                modifier = Modifier.fillMaxWidth(),
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                CutState.entries.forEach { s ->
                    FilterChip(
                        selected = editor.state == s,
                        onClick = { onChange { it.copy(state = s) } },
                        label = { Text(s.label) },
                    )
                }
            }
            Button(onClick = onSave, modifier = Modifier.fillMaxWidth()) { Text("Guardar") }
        }
    }
}