package com.example.arrax.domain.usecase.customer

import com.example.arrax.domain.repository.CustomerRepository
import jakarta.inject.Inject

class DeleteCustomerUseCase @Inject constructor(private val repo: CustomerRepository) {
    operator fun invoke(tenantId: String, customerId: String): Result<Unit> =
        runCatching { repo.deleteCustomer(tenantId, customerId) }
}