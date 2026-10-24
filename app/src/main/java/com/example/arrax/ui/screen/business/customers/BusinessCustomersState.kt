package com.example.arrax.ui.screen.business.customers
enum class CustomersTab { All, Frequent, New }

data class CustomerUi(
    val id: String,
    val initials: String,
    val name: String,
    val phone: String,
    val purchases: Int
)

data class BusinessCustomersState(
    val query: String = "",
    val selectedTab: CustomersTab = CustomersTab.All,
    val allCount: Int = 0,
    val frequentCount: Int = 0,
    val newCount: Int = 0,
    val customers: List<CustomerUi> = emptyList()
)