package com.example.arrax.ui.screen.business.more.subscreen.catalog

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.arrax.core.designsystem.components.backgrounds.ArxBackground
import com.example.arrax.core.designsystem.components.backgrounds.ArxDecoration
import com.example.arrax.core.designsystem.components.backgrounds.montain.ArxMountains
import com.example.arrax.core.designsystem.components.navigation.ArxTopBar

@Composable
fun BusinessCatalogScreen(
    modifier: Modifier=Modifier
) {
    BusinessCatalogContent(
        modifier=modifier
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun BusinessCatalogContent(
    modifier: Modifier = Modifier
){
    ArxBackground (modifier, decoration = ArxDecoration.None){
        Column(modifier= Modifier.fillMaxSize()) {
            ArxTopBar(title = "Catalog", centeredTitle = true)

        }

    }
}