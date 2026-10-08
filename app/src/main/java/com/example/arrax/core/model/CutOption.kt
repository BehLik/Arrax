package com.example.arrax.core.model

import androidx.annotation.DrawableRes

data class CutOption(
    val id: String,                       // fijo para siempre: es lo que guardas en Firestore
    val name: String,
    val state: CutState,
    @DrawableRes val imageRes: Int? = null, // cortes por defecto (van dentro de la app)
    val imageUrl: String? = null,           // cortes que agregue el usuario (Firebase Storage + Coil)
    val description: String? = null,
)