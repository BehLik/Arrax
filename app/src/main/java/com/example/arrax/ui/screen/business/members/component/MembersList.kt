package com.example.arrax.ui.screen.business.members.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.theme.ArxSpacing
import com.example.arrax.ui.screen.business.members.MemberUi

@Composable
internal fun MembersList(
    members: List<MemberUi>,
    onMemberClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = ArxSpacing.md, vertical = ArxSpacing.xs),
        verticalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
    ) {
        item(key = "header") {
            Text(
                text = "${members.size} integrantes",
                modifier = Modifier.padding(vertical = ArxSpacing.xs),
                style = MaterialTheme.typography.titleMedium
            )
        }
        items(members, key = { it.id }) { member ->
            MemberRow(member = member, onClick = onMemberClick)
        }
    }
}