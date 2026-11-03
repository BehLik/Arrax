package com.example.arrax.data.repository

import com.example.arrax.data.mapper.toDto
import com.example.arrax.data.remote.FirestoreCustomerDataSource
import com.example.arrax.domain.model.Customer
import com.example.arrax.domain.repository.CustomerRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.arrax.data.mapper.toDomain

@Singleton
class CustomerRepositoryImpl @Inject constructor(
    private val dataSource: FirestoreCustomerDataSource
) : CustomerRepository {

    override fun observeCustomers(tenantId: String): Flow<List<Customer>> =
        dataSource.observeCustomers(tenantId).map { list ->
            list.map { it.toDomain() }.sortedBy { it.name.lowercase() }
        }

    override fun observeCustomer(tenantId: String, customerId: String): Flow<Customer?> =
        dataSource.observeCustomer(tenantId, customerId).map { it?.toDomain() }

    override fun newCustomerId(tenantId: String) = dataSource.newId(tenantId)

    override fun createCustomer(tenantId: String, customer: Customer) =
        dataSource.create(tenantId, customer.toDto())

    override fun updateCustomer(tenantId: String, customer: Customer) =
        dataSource.update(tenantId, customer.toDto())

    override fun deleteCustomer(tenantId: String, customerId: String) =
        dataSource.delete(tenantId, customerId)
}