package com.example.arrax.ui.screen.business.more.component

import androidx.annotation.DrawableRes
import com.example.arrax.core.designsystem.icons.ArxIcons

internal data class MoreOption(
    @DrawableRes val icon: Int,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit
)

internal fun adminOptions(
    onTeamClick: () -> Unit,
    onCatalogClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onSettingsClick: () -> Unit
): List<MoreOption> = listOf(
    MoreOption(ArxIcons.TeamOutline, "Equipo y permisos", "Miembros, roles, invitaciones, QR y códigos", onTeamClick),
    MoreOption(ArxIcons.Box, "Catálogo de productos y cortes", "Productos disponibles y configuración", onCatalogClick),
    MoreOption(ArxIcons.Timeline, "Historial del negocio", "Eventos cerrados y resultados históricos", onHistoryClick),
    MoreOption(ArxIcons.Settings, "Ajustes del negocio", "Datos del negocio y preferencias", onSettingsClick)
)

internal fun infoOptions(
    appVersion: String,
    onHelpClick: () -> Unit,
    onAboutClick: () -> Unit
): List<MoreOption> = listOf(
    MoreOption(ArxIcons.Info, "Ayuda y soporte", "Centro de ayuda y contacto", onHelpClick),
    MoreOption(ArxIcons.System, "Acerca de Arrax", "Versión $appVersion", onAboutClick)
)