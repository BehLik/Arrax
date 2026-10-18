package com.example.arrax.core.model

import com.example.arrax.R

/**
 * Cortes básicos: funcionan sin internet. Los personalizados se suman desde Firestore.
 * Los ids son en inglés y fijos para siempre (se guardan en Firestore).
 */
val DefaultCuts = listOf(
    CutOption("meat", "Carne", CutState.FRESH, R.drawable.img_cut_meat),
    CutOption("ribs", "Costilla", CutState.FRESH, R.drawable.img_cut_ribs),
    CutOption("backbone", "Espinazo", CutState.FRESH, R.drawable.img_cut_backbone),
    CutOption("hock", "Codillos", CutState.COOKED, R.drawable.img_cut_hock),
    CutOption("cracklings", "Chicharra", CutState.COOKED, R.drawable.img_cut_cracklings),
    CutOption("blood_sausage", "Morcilla", CutState.COOKED, R.drawable.img_cut_blood_sausage),
    CutOption(
        id = "liver",
        name = "Higadilla",
        state = CutState.COOKED,
        imageRes = R.drawable.img_cut_liver,
        description = "Hígado con carne picada en cuadritos y sofrito con recado rojo.",
    ),
    CutOption("lard", "Manteca", CutState.COOKED, R.drawable.img_cut_lard),
    // Added: the Firestore seed had 9 cuts including "cabeza"; the local list had 8. No image yet.
    CutOption("head", "Cabeza", CutState.FRESH),
)
