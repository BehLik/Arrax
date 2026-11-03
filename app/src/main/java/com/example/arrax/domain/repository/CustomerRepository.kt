package com.example.arrax.domain.repository

import com.example.arrax.domain.model.Customer
import kotlinx.coroutines.flow.Flow

interface CustomerRepository {
    fun observeCustomers(tenantId: String): Flow<List<Customer>>
    fun observeCustomer(tenantId: String, customerId: String): Flow<Customer?>
    fun newCustomerId(tenantId: String): String
    fun createCustomer(tenantId: String, customer: Customer)
    fun updateCustomer(tenantId: String, customer: Customer)
    fun deleteCustomer(tenantId: String, customerId: String)
}