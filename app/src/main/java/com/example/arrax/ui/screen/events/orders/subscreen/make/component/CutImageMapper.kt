package com.example.arrax.ui.screen.events.orders.subscreen.make.component

import com.example.arrax.core.designsystem.components.cards.ArxCatalogImage
import com.example.arrax.domain.model.CutCatalog

fun CutCatalog.toCatalogImage(): ArxCatalogImage = when {
    !imageUrl.isNullOrBlank() -> ArxCatalogImage.Remote(imageUrl)
    imageRes != null -> ArxCatalogImage.Resource(imageRes)
    else -> ArxCatalogImage.None
}