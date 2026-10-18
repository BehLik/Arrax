package com.example.arrax.domain.usecase.business

import com.example.arrax.domain.model.BusinessSummary
import com.example.arrax.domain.model.Role
import com.example.arrax.domain.repository.TenantRepository
import com.example.arrax.domain.usecase.event.create.ObserveEventsUseCase
import com.example.arrax.domain.usecase.user.ObserveCurrentUserUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveMyBusinessesUseCase @Inject constructor(
    private val tenantRepository: TenantRepository,
    private val observeCurrentUserUseCase: ObserveCurrentUserUseCase,
    private val observeEventsUseCase: ObserveEventsUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<List<BusinessSummary>> =
        combine(
            tenantRepository.observeCurrentTenant(),
            observeCurrentUserUseCase()
        ) { tenant, user -> tenant to user }
            .flatMapLatest { (tenant, user) ->
                if (tenant == null) {
                    flowOf(emptyList())
                } else {
                    observeEventsUseCase(tenant.id).map { eventos ->
                        listOf(
                            BusinessSummary(
                                tenant = tenant,
                                isAdmin = user?.role == Role.ADMIN,
                                totalEvents = eventos.size
                            )
                        )
                    }
                }
            }
}