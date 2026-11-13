package com.example.arrax.ui.screen.business.customers

import java.text.Normalizer

enum class CustomersTab { All, Frequent, New }

data class CustomerSection(val letter: Char, val customers: List<CustomerUi>)
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
    val selectMode: Boolean = false,
    val allCount: Int = 0,
    val frequentCount: Int = 0,
    val newCount: Int = 0,
    val customers: List<CustomerUi> = emptyList()
)
private val combiningMarks = Regex("\\p{M}+")

// "Ángel" -> "angel", "Ñandú" -> "nandu": ordena sin importar acentos ni mayúsculas en la listaas
private fun String.sortKey(): String =
    Normalizer.normalize(trim(), Normalizer.Form.NFD)
        .replace(combiningMarks, "")
        .lowercase()

private fun String.sectionLetter(): Char {
    val c = sortKey().firstOrNull()?.uppercaseChar()
    return if (c != null && c in 'A'..'Z') c else '#'
}

/** A–Z; los nombres que no empiezan con letra van al final bajo "#". */
internal fun List<CustomerUi>.toSections(): List<CustomerSection> =
    sortedBy { it.name.sortKey() }
        .groupBy { it.name.sectionLetter() }
        .map { (letter, list) -> CustomerSection(letter, list) }
        .sortedBy { if (it.letter == '#') Char.MAX_VALUE else it.letter }