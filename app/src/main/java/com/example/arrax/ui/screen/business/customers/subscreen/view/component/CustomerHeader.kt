package com.example.arrax.ui.screen.business.customers.subscreen.view.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.arrax.core.designsystem.components.display.ArxAvatar
import com.example.arrax.core.designsystem.components.display.arxInitials
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.domain.model.Customer

@Composable
internal fun CustomerHeader(customer: Customer, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.sm),
    ) {
        ArxAvatar(
            initials = arxInitials(customer.name),
            imageUrl = customer.photoUrl,
            size = 96.dp,
        )
        Text(
            text = customer.name,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
    }
}