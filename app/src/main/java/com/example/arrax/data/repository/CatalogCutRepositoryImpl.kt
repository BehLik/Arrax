package com.example.arrax.data.repository


import com.example.arrax.core.model.DefaultCuts
import com.example.arrax.data.mapper.toCatalog
import com.example.arrax.data.mapper.toDomain
import com.example.arrax.data.remote.CutCatalogDto
import com.example.arrax.data.remote.FirestoreCatalogDataSource
import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.domain.repository.CutCatalogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CatalogCutRepositoryImpl @Inject constructor(
    private val catalogDataSource: FirestoreCatalogDataSource
) : CutCatalogRepository {

    override fun observeCatalog(tenantId: String) =
        catalogDataSource.observeCatalog(tenantId).map { it.toCatalog() }

    override fun newCutId(tenantId: String) = catalogDataSource.newId(tenantId)

    override suspend fun saveCut(tenantId: String, cut: CutCatalog) =
        catalogDataSource.upsert(
            tenantId,
            CutCatalogDto(
                id = cut.id,
                name = cut.name.trim(),
                state = cut.state.name,
                description = cut.description?.trim()
                    .orEmpty(), // "" = el usuario la dejó vacía a propósito
                imageUrl = cut.imageUrl,
                active = cut.active,
            )
        )

    override suspend fun deleteCut(tenantId: String, cutId: String) =
        catalogDataSource.delete(tenantId, cutId)

    override suspend fun resetCut(tenantId: String, cutId: String) {
        val d = DefaultCuts.firstOrNull { it.id == cutId } ?: return
        catalogDataSource.upsert(tenantId, CutCatalogDto(d.id, d.name, d.state.name, d.description, d.imageUrl, true))
    }

    override suspend fun seedDefaultCatalog(tenantId: String) =
        catalogDataSource.seedIfEmpty(tenantId)
}
