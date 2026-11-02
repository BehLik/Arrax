package com.example.arrax.domain.usecase.catalog

import com.example.arrax.domain.repository.CutCatalogRepository
import jakarta.inject.Inject

class ResetCatalogCutUseCase @Inject constructor(private val repo: CutCatalogRepository) {
    suspend operator fun invoke(tenantId: String, cutId: String): Result<Unit> =
        runCatching { repo.resetCut(tenantId, cutId) }
}