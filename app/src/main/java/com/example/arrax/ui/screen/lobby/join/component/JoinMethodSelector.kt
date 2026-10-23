package com.example.arrax.ui.screen.lobby.join.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.model.JoinMethod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JoinMethodSelector(
    selected: JoinMethod,
    onSelected: (JoinMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    val options = JoinMethod.entries
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = option == selected,
                onClick = { onSelected(option) },
                shape = SegmentedButtonDefaults.itemShape(index, options.size),
                label = { Text(if (option == JoinMethod.CODE) "Por código" else "Por QR") }
            )
        }
    }
}