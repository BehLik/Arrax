//package com.example.arrax.ui.screen.business
//
//import androidx.annotation.DrawableRes
//import androidx.compose.foundation.BorderStroke
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.horizontalScroll
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.ColumnScope
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.RowScope
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.heightIn
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.rememberScrollState
//import androidx.compose.foundation.selection.selectable
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.semantics.Role
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import com.example.arrax.core.designsystem.icons.ArxIcon
//import com.example.arrax.core.designsystem.icons.ArxIcons
//import com.example.arrax.core.designsystem.theme.ArxSpacing
//import com.example.arrax.core.designsystem.theme.extendedColors
//
///** Tono semántico de un elemento. Se resuelve siempre con tokens del tema. */
//enum class BusinessTone { Primary, Success, Warning, Info, Error, Neutral }
//
//@Composable
//internal fun BusinessTone.color(): Color = when (this) {
//    BusinessTone.Primary -> MaterialTheme.colorScheme.primary
//    BusinessTone.Success -> MaterialTheme.extendedColors.success.color
//    BusinessTone.Warning -> MaterialTheme.extendedColors.warning.color
//    BusinessTone.Info -> MaterialTheme.extendedColors.info.color
//    BusinessTone.Error -> MaterialTheme.colorScheme.error
//    BusinessTone.Neutral -> MaterialTheme.colorScheme.onSurfaceVariant
//}
///** Raíz de cada pantalla: transparente para que se vea ArxBackground del host. */
//@Composable
//internal fun BusinessScreenSurface(
//    modifier: Modifier = Modifier,
//    content: @Composable () -> Unit
//) {
//    Surface(
//        modifier = modifier.fillMaxSize(),
//        color = Color.Transparent,
//        contentColor = MaterialTheme.colorScheme.onSurface,
//        content = content
//    )
//}
//
///** Superficie glass sutil. Con [tone] se tiñe el borde y el fondo (alertas/banners). */
//@Composable
//internal fun BusinessCard(
//    modifier: Modifier = Modifier,
//    tone: BusinessTone? = null,
//    onClick: (() -> Unit)? = null,
//    contentPadding: PaddingValues = PaddingValues(ArxSpacing.md),
//    content: @Composable ColumnScope.() -> Unit
//) {
//    val glass = MaterialTheme.extendedColors
//    val container = tone?.color()?.copy(alpha = 0.12f) ?: glass.glass
//    val border = tone?.color()?.copy(alpha = 0.5f) ?: glass.glassBorder
//    Surface(
//        modifier = modifier,
//        shape = MaterialTheme.shapes.medium,
//        color = container,
//        border = BorderStroke(1.dp, border)
//    ) {
//        Column(
//            modifier = Modifier
//                .then(
//                    if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick)
//                    else Modifier
//                )
//                .padding(contentPadding),
//            content = content
//        )
//    }
//}
//
//@Composable
//internal fun BusinessToneBadge(
//    @DrawableRes icon: Int,
//    tone: BusinessTone,
//    modifier: Modifier = Modifier,
//    size: Dp = ArxSpacing.huge
//) {
//    Surface(
//        modifier = modifier.size(size),
//        shape = MaterialTheme.shapes.medium,
//        color = tone.color().copy(alpha = 0.18f),
//        contentColor = tone.color()
//    ) {
//        Box(contentAlignment = Alignment.Center) {
//            ArxIcon(icon = icon, tint = tone.color())
//        }
//    }
//}
//
//@Composable
//internal fun BusinessPill(text: String, tone: BusinessTone, modifier: Modifier = Modifier) {
//    Surface(
//        modifier = modifier,
//        shape = MaterialTheme.shapes.extraLarge,
//        color = tone.color().copy(alpha = 0.18f),
//        contentColor = tone.color()
//    ) {
//        Text(
//            text = text,
//            modifier = Modifier.padding(horizontal = ArxSpacing.sm, vertical = ArxSpacing.xxs),
//            style = MaterialTheme.typography.labelMedium,
//            maxLines = 1
//        )
//    }
//}
//
//@Composable
//internal fun BusinessAvatar(initials: String, modifier: Modifier = Modifier, size: Dp = ArxSpacing.huge) {
//    Surface(
//        modifier = modifier.size(size),
//        shape = CircleShape,
//        color = MaterialTheme.colorScheme.primaryContainer,
//        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
//    ) {
//        Box(contentAlignment = Alignment.Center) {
//            Text(text = initials, style = MaterialTheme.typography.titleSmall)
//        }
//    }
//}
//
//@Composable
//internal fun BusinessTopBar(
//    title: String,
//    modifier: Modifier = Modifier,
//    onBack: (() -> Unit)? = null,
//    actions: @Composable RowScope.() -> Unit = {}
//) {
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(horizontal = ArxSpacing.xs, vertical = ArxSpacing.xs),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        if (onBack != null) {
//            IconButton(onClick = onBack) {
//                ArxIcon(icon = ArxIcons.Back, contentDescription = "Volver")
//            }
//        } else {
//            Spacer(Modifier.width(ArxSpacing.sm))
//        }
//        Text(
//            text = title,
//            modifier = Modifier.weight(1f),
//            style = MaterialTheme.typography.headlineMedium,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//        actions()
//    }
//}
//
//@Composable
//internal fun BusinessSectionHeader(
//    title: String,
//    modifier: Modifier = Modifier,
//    actionLabel: String? = null,
//    onAction: () -> Unit = {}
//) {
//    Row(
//        modifier = modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            text = title,
//            modifier = Modifier.weight(1f, fill = false),
//            style = MaterialTheme.typography.titleMedium
//        )
//        if (actionLabel != null) {
//            TextButton(onClick = onAction) {
//                Text(actionLabel, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
//                ArxIcon(icon = ArxIcons.Forward, tint = MaterialTheme.colorScheme.primary, size = 16.dp)
//            }
//        }
//    }
//}
//
//@Composable
//internal fun BusinessTabs(
//    labels: List<String>,
//    selectedIndex: Int,
//    onSelect: (Int) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val colors = MaterialTheme.colorScheme
//    val glass = MaterialTheme.extendedColors
//    Row(
//        modifier = modifier.horizontalScroll(rememberScrollState()),
//        horizontalArrangement = Arrangement.spacedBy(ArxSpacing.xs)
//    ) {
//        labels.forEachIndexed { index, label ->
//            val selected = index == selectedIndex
//            Box(
//                modifier = Modifier
//                    .heightIn(min = 48.dp)
//                    .selectable(selected = selected, role = Role.Tab, onClick = { onSelect(index) }),
//                contentAlignment = Alignment.Center
//            ) {
//                Surface(
//                    shape = MaterialTheme.shapes.extraLarge,
//                    color = if (selected) colors.primary else glass.glass,
//                    contentColor = if (selected) colors.onPrimary else colors.onSurface,
//                    border = if (selected) null else BorderStroke(1.dp, glass.glassBorder)
//                ) {
//                    Text(
//                        text = label,
//                        modifier = Modifier.padding(horizontal = ArxSpacing.md, vertical = ArxSpacing.xs),
//                        style = MaterialTheme.typography.labelLarge,
//                        maxLines = 1
//                    )
//                }
//            }
//        }
//    }
//}
//
///** Fila estándar: [leading] + textos + [trailing] + chevron. */
//@Composable
//internal fun BusinessListRow(
//    title: String,
//    modifier: Modifier = Modifier,
//    subtitle: String? = null,
//    detail: String? = null,
//    tone: BusinessTone? = null,
//    showChevron: Boolean = true,
//    leading: @Composable (() -> Unit)? = null,
//    trailing: @Composable (() -> Unit)? = null,
//    onClick: (() -> Unit)? = null
//) {
//    BusinessCard(modifier = modifier.fillMaxWidth(), tone = tone, onClick = onClick) {
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.spacedBy(ArxSpacing.sm)
//        ) {
//            leading?.invoke()
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = title,
//                    style = MaterialTheme.typography.titleSmall,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//                if (subtitle != null) {
//                    Text(
//                        text = subtitle,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//                if (detail != null) {
//                    Text(
//                        text = detail,
//                        style = MaterialTheme.typography.bodySmall,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//            }
//            trailing?.invoke()
//            if (showChevron) {
//                ArxIcon(
//                    icon = ArxIcons.Forward,
//                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
//                    size = 20.dp
//                )
//            }
//        }
//    }
//}
//
//@Composable
//internal fun BusinessDot(tone: BusinessTone, modifier: Modifier = Modifier) {
//    Box(modifier = modifier.size(8.dp).background(tone.color(), CircleShape))
//}