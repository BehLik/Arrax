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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Surface
import androidx.compose.ui.tooling.preview.Preview
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
    onClick: (() -> Unit)? = null,
    onMenuClick: (() -> Unit)? = null,
) {
    val colors = MaterialTheme.colorScheme
    val shape = RoundedCornerShape(20.dp)

    val sizeModifier = when (variant) {
        ArxCatalogCardVariant.Horizontal -> Modifier.width(168.dp)
        else -> Modifier
    }

    Box(
        modifier = modifier
            .then(sizeModifier)
            .clip(shape)
            .background(colors.surface)
            .border(BorderStroke(1.dp, colors.primary.copy(alpha = 0.25f)), shape)
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
    ) {
        when (variant) {
            ArxCatalogCardVariant.List -> ListLayout(title, subtitle, image, onMenuClick)
            else -> VerticalLayout(title, subtitle, image, onMenuClick)
        }
    }
}

@Composable
private fun VerticalLayout(
    title: String,
    subtitle: String?,
    image: ArxCatalogImage,
    onMenuClick: (() -> Unit)?,
) {
    Column(Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp)
                .background(glow()),
            contentAlignment = Alignment.Center,
        ) {
            CatalogImage(
                image = image,
                title = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
            )
        }
        CatalogTextRow(title, subtitle, onMenuClick, Modifier.padding(start = 12.dp, end = 4.dp, top = 4.dp, bottom = 4.dp))
    }
}

@Composable
private fun ListLayout(
    title: String,
    subtitle: String?,
    image: ArxCatalogImage,
    onMenuClick: (() -> Unit)?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(108.dp)
                .height(84.dp)
                .background(glow()),
            contentAlignment = Alignment.Center,
        ) {
            CatalogImage(
                image = image,
                title = title,
                modifier = Modifier
                    .width(108.dp)
                    .height(84.dp)
                    .padding(8.dp),
            )
        }
        CatalogTextRow(
            title = title,
            subtitle = subtitle,
            onMenuClick = onMenuClick,
            modifier = Modifier
                .weight(1f)
                .padding(start = 12.dp, end = 4.dp),
        )
    }
}

@Composable
private fun CatalogTextRow(
    title: String,
    subtitle: String?,
    onMenuClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
) {
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
        if (onMenuClick != null) {
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "Opciones de $title",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun CatalogImage(
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
@Preview(name = "Grid Variant - Con Imagen y Menú", group = "ArxCatalogCard")
@Composable
private fun ArxCatalogGridCardPreview() {
    ArxTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            ArxCatalogGridCard(
                title = "Corte Ribeye",
                subtitle = "$250.00 / kg",
                image = ArxCatalogImage.None, // Cambiar por ArxCatalogImage.Resource(R.drawable.tu_imagen) si tienes un recurso
                variant = ArxCatalogCardVariant.Grid,
                onClick = {},
                onMenuClick = {}
            )
        }
    }
}

@Preview(name = "List Variant - Fila Completa", group = "ArxCatalogCard")
@Composable
private fun ArxCatalogListCardPreview() {
    ArxTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            ArxCatalogGridCard(
                title = "Arrachera Fina",
                subtitle = "Disponible en sucursal Norte",
                image = ArxCatalogImage.None,
                variant = ArxCatalogCardVariant.List,
                onClick = {},
                onMenuClick = {}
            )
        }
    }
}

@Preview(name = "Horizontal Variant - Carrusel", group = "ArxCatalogCard")
@Composable
private fun ArxCatalogHorizontalCardPreview() {
    ArxTheme {
        Surface(modifier = Modifier.padding(16.dp)) {
            ArxCatalogGridCard(
                title = "T-Bone",
                subtitle = "Premium",
                image = ArxCatalogImage.None,
                variant = ArxCatalogCardVariant.Horizontal,
                onClick = {}
            )
        }
    }
}

@Preview(name = "Grid Completo (Dark/Light)", showBackground = true, backgroundColor = 0xFF121212, group = "ArxCatalogCard")
@Composable
private fun ArxCatalogGridCollectionPreview() {
    ArxTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Variante Lista
                ArxCatalogGridCard(
                    title = "Tomahawk",
                    subtitle = "Pieza de 1.2 kg",
                    image = ArxCatalogImage.None,
                    variant = ArxCatalogCardVariant.List,
                    onClick = {},
                    onMenuClick = {}
                )

                // Variante Grid en una pequeña cuadrícula simulada
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        ArxCatalogGridCard(
                            title = "New York",
                            subtitle = "$190.00",
                            image = ArxCatalogImage.None,
                            variant = ArxCatalogCardVariant.Grid,
                            onClick = {}
                        )
                    }
                    item {
                        ArxCatalogGridCard(
                            title = "Sirloin",
                            subtitle = "$150.00",
                            image = ArxCatalogImage.None,
                            variant = ArxCatalogCardVariant.Grid,
                            onClick = {},
                            onMenuClick = {}
                        )
                    }
                }
            }
        }
    }
}