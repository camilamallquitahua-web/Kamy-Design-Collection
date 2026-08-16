package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.GalleryItem
import com.example.ui.theme.StarGold

@Composable
fun HeroCarousel(
  items: List<GalleryItem>,
  onItemClick: (GalleryItem) -> Unit,
  onToggleLike: (String) -> Unit,
  onToggleSave: (String) -> Unit,
  modifier: Modifier = Modifier
) {
  if (items.isEmpty()) return

  Column(modifier = modifier) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = "Destacados de la Semana",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "Colecciones visuales seleccionadas a mano",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    LazyRow(
      contentPadding = PaddingValues(horizontal = 16.dp),
      horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
      items(items, key = { it.id }) { item ->
        HeroCard(
          item = item,
          onClick = { onItemClick(item) },
          onToggleLike = { onToggleLike(item.id) },
          onToggleSave = { onToggleSave(item.id) }
        )
      }
    }
  }
}

@Composable
fun HeroCard(
  item: GalleryItem,
  onClick: () -> Unit,
  onToggleLike: () -> Unit,
  onToggleSave: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .width(300.dp)
      .height(220.dp)
      .clip(RoundedCornerShape(20.dp))
      .clickable(onClick = onClick)
      .testTag("hero_card_${item.id}"),
    shape = RoundedCornerShape(20.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(item.imageUrl)
          .crossfade(true)
          .build(),
        contentDescription = item.title,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
      )

      // Gradient overlay
      Box(
        modifier = Modifier
          .fillMaxSize()
          .background(
            Brush.verticalGradient(
              colors = listOf(
                Color.Black.copy(alpha = 0.3f),
                Color.Transparent,
                Color.Black.copy(alpha = 0.85f)
              )
            )
          )
      )

      // Top badges & actions
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f),
          shape = RoundedCornerShape(12.dp)
        ) {
          Text(
            text = item.category.displayName,
            color = Color.White,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
          )
        }

        Row(
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Surface(
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.45f),
            modifier = Modifier.size(36.dp)
          ) {
            IconButton(
              onClick = onToggleLike,
              modifier = Modifier.testTag("like_button_${item.id}")
            ) {
              Icon(
                imageVector = if (item.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Me gusta",
                tint = if (item.isLiked) MaterialTheme.colorScheme.primary else Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
          }

          Surface(
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.45f),
            modifier = Modifier.size(36.dp)
          ) {
            IconButton(
              onClick = onToggleSave,
              modifier = Modifier.testTag("save_button_${item.id}")
            ) {
              Icon(
                imageVector = if (item.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = "Guardar",
                tint = if (item.isSaved) Color(0xFFFFD54F) else Color.White,
                modifier = Modifier.size(18.dp)
              )
            }
          }
        }
      }

      // Bottom info
      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(14.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(bottom = 2.dp)
        ) {
          Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.85f),
            modifier = Modifier.size(13.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = item.location,
            color = Color.White.copy(alpha = 0.85f),
            style = MaterialTheme.typography.labelSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
          )
        }

        Text(
          text = item.title,
          color = Color.White,
          style = MaterialTheme.typography.titleMedium.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 17.sp
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )

        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Star,
              contentDescription = null,
              tint = StarGold,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(3.dp))
            Text(
              text = item.rating.toString(),
              color = Color.White,
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "(${item.reviewsCount})",
              color = Color.White.copy(alpha = 0.7f),
              style = MaterialTheme.typography.labelSmall
            )
          }

          if (item.priceTag != null) {
            Text(
              text = item.priceTag,
              color = Color(0xFFFFE082),
              style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold)
            )
          }
        }
      }
    }
  }
}
