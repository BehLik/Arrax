package com.example.arrax.domain.model

import androidx.annotation.DrawableRes
import com.example.arrax.domain.model.CutState

enum class CutOrigin { DEFAULT, MODIFIED, CUSTOM }

data class CutCatalog(
    val id: String = "",
    val name: String = "",
    val state: CutState = CutState.FRESH,
    val description: String? = null,
    val imageUrl: String? = null,
    @DrawableRes val imageRes: Int? = null,
    val active: Boolean = true,
    val origin: CutOrigin = CutOrigin.CUSTOM,
)