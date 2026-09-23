package com.example.arrax.data.repository


import com.example.arrax.data.mapper.toDomain
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

    override fun observeCatalog(tenantId: String): Flow<List<CutCatalog>> =
        catalogDataSource.observeCatalog(tenantId).map { list -> list.map { it.toDomain() } }

    override suspend fun seedDefaultCatalog(tenantId: String) =
        catalogDataSource.seedIfEmpty(tenantId)
}
