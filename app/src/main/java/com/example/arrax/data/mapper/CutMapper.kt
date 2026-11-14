package com.example.arrax.data.mapper

import com.example.arrax.core.designsystem.components.cards.ArxCatalogImage
import com.example.arrax.domain.model.CutState
import com.example.arrax.domain.model.DefaultCuts
import com.example.arrax.data.remote.CutCatalogDto
import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.domain.model.CutOrigin

fun CutCatalogDto.toDomain(): CutCatalog {
    val default = DefaultCuts.firstOrNull { it.id == id }
    val cutState = state?.let { runCatching { CutState.valueOf(it) }.getOrNull() }
        ?: default?.state ?: CutState.FRESH
    val desc = (description ?: default?.description)?.ifBlank { null }

    val origin = when {
        default == null -> CutOrigin.CUSTOM
        default.name != name ||
                default.state != cutState ||
                default.description.orEmpty() != desc.orEmpty() -> CutOrigin.MODIFIED
        else -> CutOrigin.DEFAULT
    }
    return CutCatalog(
        id = id, name = name, state = cutState, description = desc,
        imageUrl = imageUrl, imageRes = default?.imageRes,
        active = active, origin = origin,
    )
}

/** Firestore manda; lo que falte de DefaultCuts se completa (offline, sin seed, doc borrado). */
fun List<CutCatalogDto>.toCatalog(): List<CutCatalog> {
    val remote = map { it.toDomain() }
    val missing = DefaultCuts
        .filter { d -> remote.none { it.id == d.id } }
        .map { d -> CutCatalog(d.id, d.name, d.state, d.description, d.imageUrl, d.imageRes, true, CutOrigin.DEFAULT) }
    val order = DefaultCuts.map { it.id }
    return (remote + missing).sortedWith(
        compareBy<CutCatalog> { it.origin == CutOrigin.CUSTOM }
            .thenBy { order.indexOf(it.id).takeIf { i -> i >= 0 } ?: Int.MAX_VALUE }
            .thenBy { it.name.lowercase() }
    )
}