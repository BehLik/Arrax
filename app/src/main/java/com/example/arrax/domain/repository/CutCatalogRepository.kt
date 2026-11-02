package com.example.arrax.domain.repository

import com.example.arrax.domain.model.CutCatalog
import kotlinx.coroutines.flow.Flow

interface CutCatalogRepository {
    fun observeCatalog(tenantId: String): Flow<List<CutCatalog>>
    suspend fun seedDefaultCatalog(tenantId: String)
    fun newCutId(tenantId: String): String
    suspend fun saveCut(tenantId: String, cut: CutCatalog)
    suspend fun deleteCut(tenantId: String, cutId: String)
    suspend fun resetCut(tenantId: String, cutId: String)
}