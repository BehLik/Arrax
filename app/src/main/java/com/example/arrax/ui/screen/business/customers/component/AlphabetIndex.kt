package com.example.arrax.ui.screen.business.customers.component

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
internal fun AlphabetIndex(
    letters: List<Char>,
    selected: Char?,
    onLetterSelected: (Char) -> Unit,
    modifier: Modifier = Modifier
) {
    if (letters.isEmpty()) return

    val currentLetters by rememberUpdatedState(letters)
    val currentOnSelected by rememberUpdatedState(onLetterSelected)

    val pick: (y: Float, height: Int) -> Unit = { y, height ->
        val list = currentLetters
        if (list.isNotEmpty() && height > 0) {
            val index = (y / height * list.size).toInt().coerceIn(0, list.lastIndex)
            currentOnSelected(list[index])
        }
    }

    Column(
        modifier = modifier
            .width(24.dp)
            .pointerInput(Unit) {
                detectTapGestures { pos -> pick(pos.y, size.height) }
            }
            .pointerInput(Unit) {
                detectVerticalDragGestures(
                    onDragStart = { pos -> pick(pos.y, size.height) },
                    onVerticalDrag = { change, _ -> pick(change.position.y, size.height) }
                )
            }
    ) {
        letters.forEach { letter ->
            val isSelected = letter == selected
            Box(
                modifier = Modifier.fillMaxWidth().height(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = letter.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}