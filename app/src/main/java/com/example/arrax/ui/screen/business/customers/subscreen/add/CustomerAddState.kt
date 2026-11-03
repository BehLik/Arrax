package com.example.arrax.ui.screen.business.customers.subscreen.add

data class CustomerAddState(
    val customerId: String? = null,   // null = cliente nuevo
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val reference: String = "",
    val photo: String? = null,        // url existente o uri recién elegida
    val nameError: String? = null,
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val message: String? = null,
    val finished: Boolean = false,
) {
    val isEditing get() = customerId != null
}

data class CustomerAddActions(
    val onBack: () -> Unit = {},
    val onNameChange: (String) -> Unit = {},
    val onPhoneChange: (String) -> Unit = {},
    val onAddressChange: (String) -> Unit = {},
    val onReferenceChange: (String) -> Unit = {},
    val onPhotoPicked: (String) -> Unit = {},
    val onSave: () -> Unit = {},
    val onMessageShown: () -> Unit = {},
)