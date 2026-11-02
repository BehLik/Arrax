package com.example.arrax.domain.usecase.catalog

import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.domain.repository.CutCatalogRepository
import jakarta.inject.Inject

class SaveCatalogCutUseCase @Inject constructor(private val repo: CutCatalogRepository) {
    suspend operator fun invoke(tenantId: String, cut: CutCatalog): Result<Unit> = runCatching {
        val id = cut.id.ifBlank { repo.newCutId(tenantId) }
        repo.saveCut(tenantId, cut.copy(id = id))
    }
}