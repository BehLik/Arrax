package com.example.arrax.domain.usecase.customer.Observe

import com.example.arrax.domain.model.Customer
import com.example.arrax.domain.repository.CustomerRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveCustomersUseCase @Inject constructor(private val repo: CustomerRepository) {
    operator fun invoke(tenantId: String): Flow<List<Customer>> = repo.observeCustomers(tenantId)
}