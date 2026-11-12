package com.example.arrax.ui.screen.business.customers.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.customers.CustomerSection
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CustomersIndexedList(
    sections: List<CustomerSection>,
    onCustomerClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    // Posición en el LazyColumn donde empieza cada letra (1 header + N filas por sección)
    val startIndexByLetter = remember(sections) {
        var index = 0
        buildMap<Char, Int> {
            sections.forEach {
                put(it.letter, index)
                index += 1 + it.customers.size
            }
        }
    }

    val currentLetter by remember(sections) {
        derivedStateOf {
            val first = listState.firstVisibleItemIndex
            sections.lastOrNull { startIndexByLetter.getValue(it.letter) <= first }?.letter
        }
    }

    Row(modifier = modifier) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(
                start = ArxSpacing.md,
                end = ArxSpacing.xs,
                top = ArxSpacing.xs,
                bottom = ArxSpacing.xs
            ),
            verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
        ) {
            sections.forEach { section ->
                stickyHeader(key = "header_${section.letter}") {
                    SectionHeader(section.letter)
                }
                items(section.customers, key = { it.id }) { customer ->
                    CustomerRow(customer = customer, onClick = onCustomerClick)
                }
            }
        }

        AlphabetIndex(
            letters = sections.map { it.letter },
            selected = currentLetter,
            onLetterSelected = { letter ->
                startIndexByLetter[letter]?.let { index ->
                    scope.launch { listState.scrollToItem(index) }
                }
            },
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(end = ArxSpacing.xxs)
        )
    }
}

@Composable
private fun SectionHeader(letter: Char, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = ArxSpacing.xs, vertical = ArxSpacing.xxs)
    ) {
        Text(
            text = letter.toString(),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}