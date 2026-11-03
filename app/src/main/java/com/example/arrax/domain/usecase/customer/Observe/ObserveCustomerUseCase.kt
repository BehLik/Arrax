package com.example.arrax.domain.usecase.customer.Observe

import com.example.arrax.domain.model.Customer
import com.example.arrax.domain.repository.CustomerRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveCustomerUseCase @Inject constructor(private val repo: CustomerRepository) {
    operator fun invoke(tenantId: String, customerId: String): Flow<Customer?> =
        repo.observeCustomer(tenantId, customerId)
}