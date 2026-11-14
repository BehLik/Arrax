package com.example.arrax.core.designsystem.components.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.arrax.core.designsystem.theme.ArxTheme

/** Cómo se acomoda la tarjeta. */
enum class ArxCatalogCardVariant {
    /** Vertical, ocupa el ancho que le den (LazyVerticalGrid). */
    Grid,

    /** Fila a lo ancho: imagen a la izquierda, texto al centro, menú a la derecha (LazyColumn). */
    List,

    /** Vertical de ancho fijo, pensada para carruseles (LazyRow). */
    Horizontal,
}

/** Origen de la imagen: drawable de la app, URL del usuario (Firestore) o nada. */
sealed interface ArxCatalogImage {
    data class Resource(@DrawableRes val id: Int) : ArxCatalogImage
    data class Remote(val url: String) : ArxCatalogImage
    data object None : ArxCatalogImage
}

/** Acción del menú ⋮ de la tarjeta. La pantalla decide cuáles pasar. */
data class ArxCatalogMenuAction(
    val label: String,
    val destructive: Boolean = false,
    val onClick: () -> Unit,
)

/**
 * Tarjeta de catálogo (cortes, productos, etc.).
 *
 * Las imágenes son PNG sin fondo ni bordes, así que NO se recortan (Crop) ni se les pone
 * marco: se muestran con ContentScale.Fit sobre un resplandor suave que da la tarjeta.
 */
@Composable
fun ArxCatalogGridCard(
    title: String,
    image: ArxCatalogImage,
    modifier: Modifier = Modifier,
    variant: ArxCatalogCardVariant = ArxCatalogCardVariant.Grid,
    subtitle: String? = null,
    tag: String? = null,
    muted: Boolean = false,
    onClick: (() -> Unit)? = null,
    menuActions: List<ArxCatalogMenuAction> = emptyList(),
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(20.dp)
    val sizeModifier =
        if (variant == ArxCatalogCardVariant.Horizontal) Modifier.width(168.dp) else Modifier

    Box(
        modifier = modifier
            .then(sizeModifier)
            .alpha(if (muted) 0.5f else 1f)
            .clip(shape)
            .background(colors.surface)
            .border(BorderStroke(1.dp, colors.primary.copy(alpha = 0.25f)), shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    ) {
        if (variant == ArxCatalogCardVariant.List) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(84.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CatalogMedia(
                    image = image,
                    title = title,
                    tag = tag,
                    modifier = Modifier
                        .width(108.dp)
                        .height(84.dp),
                    imagePadding = PaddingValues(8.dp),
                )
                CatalogTextRow(
                    title = title,
                    subtitle = subtitle,
                    menuActions = menuActions,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                )
            }
        } else {
            Column(Modifier.fillMaxWidth()) {
                CatalogMedia(
                    image = image,
                    title = title,
                    tag = tag,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp),
                    imagePadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                )
                CatalogTextRow(
                    title = title,
                    subtitle = subtitle,
                    menuActions = menuActions,
                    modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun CatalogMedia(
    image: ArxCatalogImage,
    title: String,
    tag: String?,
    imagePadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Box(modifier.background(glow()), contentAlignment = Alignment.Center) {
        ArxCatalogImageView(
            image = image,
            title = title,
            modifier = Modifier
                .fillMaxSize()
                .padding(imagePadding),
        )
        if (tag != null) {
            Text(
                text = tag,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .padding(horizontal = 8.dp, vertical = 2.dp),
            )
        }
    }
}

@Composable
private fun CatalogTextRow(
    title: String,
    subtitle: String?,
    menuActions: List<ArxCatalogMenuAction>,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        if (menuActions.isNotEmpty()) {
            Box {
                IconButton(onClick = { expanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Opciones de $title",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    menuActions.forEach { action ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = action.label,
                                    color = if (action.destructive) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.onSurface,
                                )
                            },
                            onClick = {
                                expanded = false
                                action.onClick()
                            },
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArxCatalogImageView(
    image: ArxCatalogImage,
    title: String,
    modifier: Modifier = Modifier,
) {
    when (image) {
        is ArxCatalogImage.Resource -> Image(
            painter = painterResource(image.id),
            contentDescription = title,
            modifier = modifier,
            contentScale = ContentScale.Fit, // PNG sin bordes: nunca Crop
        )

        is ArxCatalogImage.Remote -> AsyncImage(
            model = image.url,
            contentDescription = title,
            modifier = modifier,
            contentScale = ContentScale.Fit,
        )

        ArxCatalogImage.None -> Box(modifier, contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = title.take(1).uppercase(),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
    }
}

/** Resplandor turquesa suave detrás de la imagen (el "fondo" que se ve en el diseño). */
@Composable
private fun glow(): Brush = Brush.radialGradient(
    colors = listOf(
        MaterialTheme.colorScheme.primary.copy(alpha = 0.20f),
        Color.Transparent,
    ),
    center = Offset(60f, 40f),
    radius = 420f,
)

private val previewActions = listOf(
    ArxCatalogMenuAction("Editar") {},
    ArxCatalogMenuAction("Ocultar") {},
    ArxCatalogMenuAction("Eliminar", destructive = true) {},
)

@Preview(name = "Grid", group = "ArxCatalogCard")
@Composable
private fun ArxCatalogGridCardPreview() {
    ArxTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            ArxCatalogGridCard(
                title = "Costilla",
                subtitle = "Fresca",
                tag = "Sistema",
                image = ArxCatalogImage.None,
                variant = ArxCatalogCardVariant.Grid,
                onClick = {},
                menuActions = previewActions,
                modifier = Modifier.width(180.dp),
            )
        }
    }
}

@Preview(name = "List", group = "ArxCatalogCard")
@Composable
private fun ArxCatalogListCardPreview() {
    ArxTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            ArxCatalogGridCard(
                title = "Higadilla",
                subtitle = "Cocida · Oculto",
                tag = "Modificado",
                muted = true,
                image = ArxCatalogImage.None,
                variant = ArxCatalogCardVariant.List,
                onClick = {},
                menuActions = previewActions,
            )
        }
    }
}