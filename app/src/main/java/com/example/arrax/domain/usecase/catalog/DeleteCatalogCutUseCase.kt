package com.example.arrax.domain.usecase.catalog

import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.domain.model.CutOrigin
import com.example.arrax.domain.repository.CutCatalogRepository
import jakarta.inject.Inject

class DeleteCatalogCutUseCase @Inject constructor(private val repo: CutCatalogRepository) {
    suspend operator fun invoke(tenantId: String, cut: CutCatalog): Result<Unit> = runCatching {
        require(cut.origin == CutOrigin.CUSTOM) { "Los cortes del sistema solo se ocultan" }
        repo.deleteCut(tenantId, cut.id)
    }
}