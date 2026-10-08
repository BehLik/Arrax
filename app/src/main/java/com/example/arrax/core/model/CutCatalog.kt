package com.example.arrax.core.model
import com.example.arrax.R


/** Cortes básicos: funcionan sin internet. Los personalizados se suman desde Firestore. */
val DefaultCuts = listOf(
    CutOption("carne", "Carne", CutState.FRESH, R.drawable.img_cut_carne),
    CutOption("costilla", "Costilla", CutState.FRESH, R.drawable.img_cut_costilla),
    CutOption("espinazo", "Espinazo", CutState.FRESH, R.drawable.img_cut_espinazo),
    CutOption("codillo", "Codillos", CutState.COOKED, R.drawable.img_cut_codillo),
    CutOption("chicharra", "Chicharra", CutState.COOKED, R.drawable.img_cut_chicharra),
    CutOption("morcilla", "Morcilla", CutState.COOKED, R.drawable.img_cut_morcilla),
    CutOption(
        id = "higadilla",
        name = "Higadilla",
        state = CutState.COOKED,
        imageRes = R.drawable.img_cut_higadilla,
        description = "Hígado con carne picada en cuadritos y sofrito con recado rojo.",
    ),
    CutOption("manteca", "Manteca", CutState.COOKED, R.drawable.img_cut_manteca),
)