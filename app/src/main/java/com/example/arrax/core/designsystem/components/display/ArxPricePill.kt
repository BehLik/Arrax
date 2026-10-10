package com.example.arrax.core.designsystem.components.display

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.core.designsystem.theme.ArxTheme

/** Píldora de precio en MXN con dos decimales (ej. $130.00). */
@Composable
fun ArxPricePill(
    amountMxn: Double,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Text(
            text = arxFormatMoney(amountMxn),
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(horizontal = ArxSpacing.sm, vertical = ArxSpacing.xxs)
        )
    }
}

@Composable
private fun ArxPricePillPreviewContent() {
    ArxTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            Row(
                modifier = Modifier.padding(ArxSpacing.md),
                horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
            ) {
                ArxPricePill(amountMxn = 130.0)
                ArxPricePill(amountMxn = 28.6)
                ArxPricePill(amountMxn = 1234.5)
            }
        }
    }
}

@Preview(name = "Precio – Light", showBackground = false)
@Composable
fun ArxPricePillPreviewLight() = ArxPricePillPreviewContent()

@Preview(name = "Precio – Dark", showBackground = false, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ArxPricePillPreviewDark() = ArxPricePillPreviewContent()