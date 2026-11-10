package com.example.arrax.core.designsystem.images

import com.example.arrax.R

/**
 * Catálogo central de imágenes y elementos de marca de ARRAX.
 *
 * Igual que ArxIcons, los nombres representan la intención dentro de la app,
 * no el nombre físico del drawable. Así se puede cambiar el recurso
 * sin tocar las pantallas.
 *
 * Ejemplo:
 * ArxImages.CutRibs
 * ArxImages.LogoMark
 */
object ArxImages {

    // ============================================================
    // MARCA
    // ============================================================

    /** Isotipo completo de ARRAX (capa exterior + interior). */
    val LogoMark = R.drawable.logo_mark

    /** Capa exterior del isotipo (útil para animaciones por capas). */
    val LogoMarkOuter = R.drawable.logo_mark_outer

    /** Capa interior del isotipo (útil para animaciones por capas). */
    val LogoMarkInner = R.drawable.logo_mark_inner

    /** Logotipo con el nombre de la marca. */
    val LogoWordmark = R.drawable.logo_wordmark

    // ============================================================
    // FONDOS
    // ============================================================

    /** Montañas decorativas del fondo (ver ArxBackground). */
    val BackgroundMountains = R.drawable.bg_mountains

    val Farm=R.drawable.img_farms
    val Meadow=R.drawable.img_meadow

    // ============================================================
    // CORTES DE CARNE
    // ============================================================

    val Cuts =R.drawable.img_cuts
    /** Carne en general / corte de carne. */
    val CutMeat = R.drawable.img_cut_meat

    /** Costillas. */
    val CutRibs = R.drawable.img_cut_ribs

    /** Espinazo. */
    val CutBackbone = R.drawable.img_cut_backbone

    /** Cabeza. */
    val CutHead = R.drawable.img_cut_head

    /** Codillo o pata. */
    val CutHock = R.drawable.img_cut_hock

    /** Hígado. */
    val CutLiver = R.drawable.img_cut_liver

    /** Manteca. */
    val CutLard = R.drawable.img_cut_lard

    /** Chicharrón. */
    val CutCracklings = R.drawable.img_cut_cracklings

    /** Moronga o morcilla. */
    val CutBloodSausage = R.drawable.img_cut_blood_sausage
}