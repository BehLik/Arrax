package com.example.arrax.core.common

object MxCatalog {
    /** Estado -> municipios. La lista de Yucatán es parcial: completa con los que uses. */
    val municipalities: Map<String, List<String>> = mapOf(
        "Yucatán" to listOf(
            "Peto", "Tzucacab", "Tekax", "Chacsinkín", "Tixméhuac", "Oxkutzcab",
            "Ticul", "Akil", "Mayapán", "Teabo", "Chumayel", "Maní",
            "Mérida", "Valladolid", "Tizimín", "Progreso",
        ),
        "Quintana Roo" to listOf(
            "Bacalar", "Benito Juárez", "Cozumel", "Felipe Carrillo Puerto",
            "Isla Mujeres", "José María Morelos", "Lázaro Cárdenas",
            "Othón P. Blanco", "Puerto Morelos", "Solidaridad", "Tulum",
        ),
    )
    val states: List<String> get() = municipalities.keys.toList()
}