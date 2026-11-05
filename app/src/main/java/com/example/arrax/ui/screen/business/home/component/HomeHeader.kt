package com.example.arrax.ui.screen.business.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.arrax.R

import com.example.arrax.core.designsystem.components.navigation.ArxTopBar
import com.example.arrax.core.designsystem.components.navigation.ArxTopBarVariant
import com.example.arrax.core.designsystem.icons.ArxIcon
import com.example.arrax.core.designsystem.icons.ArxIcons
import com.example.arrax.ui.root.session.CurrentUserAvatar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeHeader(
    onMoreClick: () -> Unit,
    onBackClik:()->Unit,
    modifier: Modifier = Modifier
) {
    ArxTopBar(
        title = "Arrax",
        subtitle = "Negocios. Personas. Resultados.",
        variant = ArxTopBarVariant.MENU,
        onNavigationClick = onBackClik,
        modifier = modifier,
        actions = {
            Box(
                modifier= Modifier
                    .minimumInteractiveComponentSize()
                    .clip(CircleShape)
                    .clickable(
                        onClickLabel = stringResource(R.string.business_more_cd),
                        onClick = onMoreClick
                    )
            ){CurrentUserAvatar(size = 36.dp)}

        }
    )
}
