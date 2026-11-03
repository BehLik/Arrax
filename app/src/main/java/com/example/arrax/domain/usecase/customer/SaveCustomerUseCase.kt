package com.example.arrax.domain.usecase.customer

import com.example.arrax.domain.model.Customer
import com.example.arrax.domain.repository.CustomerRepository
import jakarta.inject.Inject

class SaveCustomerUseCase @Inject constructor(private val repo: CustomerRepository) {
    operator fun invoke(tenantId: String, customer: Customer): Result<Unit> = runCatching {
        require(customer.name.isNotBlank()) { "El nombre es obligatorio" }
        if (customer.id.isBlank()) {
            repo.createCustomer(tenantId, customer.copy(id = repo.newCustomerId(tenantId)))
        } else {
            repo.updateCustomer(tenantId, customer)
        }
    }
}