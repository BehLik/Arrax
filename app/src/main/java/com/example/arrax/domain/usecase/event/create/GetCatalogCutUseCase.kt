package com.example.arrax.domain.usecase.event.create

import com.example.arrax.domain.model.CutCatalog
import com.example.arrax.domain.repository.CutCatalogRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCatalogCutUseCase @Inject constructor(
    private val cutCatalogRepository: CutCatalogRepository
) {
    operator fun invoke(tenantId: String): Flow<List<CutCatalog>> =
        cutCatalogRepository.observeCatalog(tenantId)
}
