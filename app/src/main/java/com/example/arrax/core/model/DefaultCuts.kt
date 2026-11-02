package com.example.arrax.core.model

import com.example.arrax.R
import com.example.arrax.core.designsystem.images.ArxImages

/**
 * Cortes básicos: funcionan sin internet. Los personalizados se suman desde Firestore.
 * Los ids son en inglés y fijos para siempre (se guardan en Firestore).
 */
val DefaultCuts = listOf(
    CutOption(
        id = "meat",
        name = "Carne",
        state = CutState.FRESH,
        imageRes = ArxImages.CutMeat,
        description = "Carne fresca de cerdo, lista para preparar o vender."
    ),
    CutOption(
        id = "ribs",
        name = "Costilla",
        state = CutState.FRESH,
        imageRes = ArxImages.CutRibs,
        description = "Costillas de cerdo frescas, ideales para asar, freír o guisar."
    ),
    CutOption(
        id = "backbone",
        name = "Espinazo",
        state = CutState.FRESH,
        imageRes = ArxImages.CutBackbone,
        description = "Espinazo de cerdo fresco, ideal para preparar caldos y guisos."
    ),
    CutOption(
        id = "hock",
        name = "Codillos",
        state = CutState.FRESH,
        imageRes = ArxImages.CutHock,
        description = "Codillos de cerdo fresco, ideal para preparar caldos y guisos."
    ),
    CutOption(
        id = "cracklings",
        name = "Chicharra",
        state = CutState.COOKED,
        imageRes = ArxImages.CutCracklings,
        description = "Chicharra de cerdo frita, crujiente y lista para consumir."
    ),
    CutOption(
        id = "blood_sausage",
        name = "Morcilla",
        state = CutState.COOKED,
        imageRes = ArxImages.CutBloodSausage,
        description = "Morcilla de cerdo cocida, preparada y lista para vender."
    ),
    CutOption(
        id = "liver",
        name = "Higadilla",
        state = CutState.COOKED,
        imageRes = ArxImages.CutLiver,
        description = "Hígado con carne picada en cuadritos y sofrito con recado rojo."
    ),
    CutOption(
        id = "lard",
        name = "Manteca",
        state = CutState.COOKED,
        imageRes = ArxImages.CutLard,
        description = "Manteca de cerdo derretida, lista para utilizar en la preparación de alimentos."
    ),
    CutOption(
        id = "head",
        name = "Cabeza",
        state = CutState.FRESH,
        imageRes = ArxImages.CutHead,
        description = "Cabeza de cerdo fresca, disponible para su preparación o venta."
    )
)
