package com.example.arrax.domain.repository

import com.example.arrax.domain.model.CutCatalog
import kotlinx.coroutines.flow.Flow

interface CutCatalogRepository {
    fun observeCatalog(tenantId: String): Flow<List<CutCatalog>>
    suspend fun seedDefaultCatalog(tenantId: String)
}
