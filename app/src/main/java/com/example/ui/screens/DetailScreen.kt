package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FolderSpecial
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.GalleryItem
import com.example.data.model.Review
import com.example.ui.theme.StarGold
import com.example.ui.viewmodel.GalleryViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(
  item: GalleryItem,
  viewModel: GalleryViewModel,
  onBack: () -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  var activeImageUrl by remember(item.id) { mutableStateOf(item.imageUrl) }
  var userRating by remember { mutableFloatStateOf(5f) }
  var reviewComment by remember { mutableStateOf("") }
  var showReviewForm by remember { mutableStateOf(false) }

  Box(
    modifier = modifier
      .fillMaxSize()
      .background(MaterialTheme.colorScheme.background)
  ) {
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .testTag("detail_screen_scroll"),
      contentPadding = PaddingValues(bottom = 96.dp)
    ) {
      // Main Hero Image Header
      item {
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(340.dp)
        ) {
          AsyncImage(
            model = ImageRequest.Builder(context)
              .data(activeImageUrl)
              .crossfade(true)
              .build(),
            contentDescription = item.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
          )

          // Gradient
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color.Black.copy(alpha = 0.6f),
                    Color.Transparent,
                    Color.Black.copy(alpha = 0.7f)
                  )
                )
              )
          )

          // Top action buttons with status bar padding
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .statusBarsPadding()
              .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = CircleShape,
              color = Color.Black.copy(alpha = 0.5f),
              modifier = Modifier.size(42.dp)
            ) {
              IconButton(
                onClick = onBack,
                modifier = Modifier.testTag("detail_back_button")
              ) {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                  contentDescription = "Volver",
                  tint = Color.White
                )
              }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
              Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier.size(42.dp)
              ) {
                IconButton(
                  onClick = {
                    Toast.makeText(context, "Enlace copiado al portapapeles", Toast.LENGTH_SHORT).show()
                  },
                  modifier = Modifier.testTag("share_button")
                ) {
                  Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Compartir",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                  )
                }
              }

              Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.5f),
                modifier = Modifier.size(42.dp)
              ) {
                IconButton(
                  onClick = { viewModel.toggleSave(item.id) },
                  modifier = Modifier.testTag("detail_save_button")
                ) {
                  Icon(
                    imageVector = if (item.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Guardar",
                    tint = if (item.isSaved) MaterialTheme.colorScheme.primary else Color.White,
                    modifier = Modifier.size(22.dp)
                  )
                }
              }
            }
          }

          // Category Badge + Price
          Row(
            modifier = Modifier
              .align(Alignment.BottomStart)
              .fillMaxWidth()
              .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              color = MaterialTheme.colorScheme.primary,
              shape = RoundedCornerShape(8.dp)
            ) {
              Text(
                text = item.category.displayName,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                color = Color.White,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }

            if (item.priceTag != null) {
              Surface(
                color = Color.Black.copy(alpha = 0.75f),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text(
                  text = item.priceTag,
                  style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                  color = Color(0xFFFFE082),
                  modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                )
              }
            }
          }
        }
      }

      // Thumbnail gallery selector
      if (item.galleryUrls.size > 1) {
        item {
          LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            items(item.galleryUrls) { imgUrl ->
              val isSelected = activeImageUrl == imgUrl
              Box(
                modifier = Modifier
                  .size(68.dp)
                  .clip(RoundedCornerShape(12.dp))
                  .clickable { activeImageUrl = imgUrl }
              ) {
                AsyncImage(
                  model = ImageRequest.Builder(context)
                    .data(imgUrl)
                    .crossfade(true)
                    .build(),
                  contentDescription = null,
                  modifier = Modifier.fillMaxSize(),
                  contentScale = ContentScale.Crop
                )
                if (isSelected) {
                  Box(
                    modifier = Modifier
                      .fillMaxSize()
                      .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.35f))
                  )
                }
              }
            }
          }
        }
      }

      // Title & Location
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
          Text(
            text = item.title,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
            color = MaterialTheme.colorScheme.onBackground
          )

          Spacer(modifier = Modifier.height(4.dp))

          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.LocationOn,
              contentDescription = null,
              tint = MaterialTheme.colorScheme.primary,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = item.location,
              style = MaterialTheme.typography.bodyMedium,
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }

          Spacer(modifier = Modifier.height(10.dp))

          // Rating summary card
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(12.dp))
              .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
              .padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = StarGold,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "${item.rating} / 5.0",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Spacer(modifier = Modifier.width(8.dp))
              Text(
                text = "(${item.reviewsCount} opiniones)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "${item.likesCount} me gusta",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
              )
            }
          }
        }
      }

      // Author Bio
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            AsyncImage(
              model = ImageRequest.Builder(context)
                .data(item.authorAvatar)
                .crossfade(true)
                .build(),
              contentDescription = item.authorName,
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape),
              contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = item.authorName,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "Fotógrafo y Creador Verificado",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
              )
            }
          }
        }
      }

      // Description
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
          Text(
            text = "Acerca de este lugar",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.height(6.dp))
          Text(
            text = item.description,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 22.sp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
      }

      // Highlights
      if (item.highlights.isNotEmpty()) {
        item {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(horizontal = 16.dp, vertical = 6.dp)
          ) {
            Text(
              text = "Puntos Destacados",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            item.highlights.forEach { highlight ->
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                Icon(
                  imageVector = Icons.Default.CheckCircle,
                  contentDescription = null,
                  tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                  text = highlight,
                  style = MaterialTheme.typography.bodyMedium,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }
          }
        }
      }

      // Tags
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
          Text(
            text = "Etiquetas",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
          Spacer(modifier = Modifier.height(6.dp))
          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            item.tags.forEach { tag ->
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant
              ) {
                Text(
                  text = "#$tag",
                  style = MaterialTheme.typography.labelSmall,
                  color = MaterialTheme.colorScheme.onSurfaceVariant,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }
        }
      }

      // Reviews Section
      item {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "Opiniones de la Comunidad (${item.userReviews.size})",
              style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onBackground
            )

            Button(
              onClick = { showReviewForm = !showReviewForm },
              colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
              shape = RoundedCornerShape(10.dp)
            ) {
              Text(
                text = if (showReviewForm) "Cancelar" else "+ Calificar",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
              )
            }
          }

          // Review Form Accordion
          AnimatedVisibility(visible = showReviewForm) {
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
              shape = RoundedCornerShape(14.dp),
              colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
              Column(modifier = Modifier.padding(14.dp)) {
                Text(
                  text = "¿Qué te pareció esta experiencia?",
                  style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                  color = MaterialTheme.colorScheme.onSurface
                )

                // Star selector
                Row(
                  modifier = Modifier.padding(vertical = 8.dp),
                  horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                  (1..5).forEach { starIndex ->
                    IconButton(
                      onClick = { userRating = starIndex.toFloat() },
                      modifier = Modifier.size(36.dp)
                    ) {
                      Icon(
                        imageVector = if (starIndex <= userRating) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = "Estrella $starIndex",
                        tint = StarGold,
                        modifier = Modifier.size(28.dp)
                      )
                    }
                  }
                }

                OutlinedTextField(
                  value = reviewComment,
                  onValueChange = { reviewComment = it },
                  placeholder = { Text("Escribe tu experiencia o recomendación...") },
                  modifier = Modifier
                    .fillMaxWidth()
                    .testTag("review_input_field"),
                  maxLines = 3
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                  onClick = {
                    if (reviewComment.isNotBlank()) {
                      viewModel.addReview(item.id, userRating, reviewComment)
                      reviewComment = ""
                      showReviewForm = false
                      Toast.makeText(context, "¡Gracias por tu opinión!", Toast.LENGTH_SHORT).show()
                    }
                  },
                  enabled = reviewComment.isNotBlank(),
                  colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                  modifier = Modifier
                    .align(Alignment.End)
                    .testTag("submit_review_button")
                ) {
                  Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                  Spacer(modifier = Modifier.width(6.dp))
                  Text("Publicar Reseña")
                }
              }
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Existing reviews
          item.userReviews.forEach { review ->
            ReviewItemView(review = review)
            Spacer(modifier = Modifier.height(8.dp))
          }
        }
      }
    }

    // Bottom Sticky Action Bar
    Surface(
      modifier = Modifier
        .align(Alignment.BottomCenter)
        .fillMaxWidth(),
      color = MaterialTheme.colorScheme.surface,
      tonalElevation = 8.dp,
      shadowElevation = 12.dp
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        IconButton(
          onClick = { viewModel.toggleLike(item.id) },
          modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Icon(
            imageVector = if (item.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Me gusta",
            tint = if (item.isLiked) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        IconButton(
          onClick = { viewModel.showAddToCollectionDialog(item) },
          modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
        ) {
          Icon(
            imageVector = Icons.Default.FolderSpecial,
            contentDescription = "Guardar en colección",
            tint = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Button(
          onClick = { viewModel.toggleSave(item.id) },
          modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .testTag("detail_primary_save_btn"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = if (item.isSaved) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary
          )
        ) {
          Icon(
            imageVector = if (item.isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
            contentDescription = null,
            modifier = Modifier.size(18.dp)
          )
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = if (item.isSaved) "Guardado en Favoritos" else "Guardar en Favoritos",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
          )
        }
      }
    }
  }
}

@Composable
fun ReviewItemView(review: Review) {
  Card(
    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(12.dp),
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
  ) {
    Column(modifier = Modifier.padding(12.dp)) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
              .data(review.userAvatar)
              .crossfade(true)
              .build(),
            contentDescription = review.userName,
            modifier = Modifier
              .size(32.dp)
              .clip(CircleShape),
            contentScale = ContentScale.Crop
          )
          Spacer(modifier = Modifier.width(8.dp))
          Column {
            Text(
              text = review.userName,
              style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
              color = MaterialTheme.colorScheme.onSurface
            )
            Text(
              text = review.date,
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = MaterialTheme.colorScheme.onSurfaceVariant
            )
          }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(
            imageVector = Icons.Default.Star,
            contentDescription = null,
            tint = StarGold,
            modifier = Modifier.size(14.dp)
          )
          Spacer(modifier = Modifier.width(3.dp))
          Text(
            text = "${review.rating}",
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
          )
        }
      }

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = review.comment,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant
      )
    }
  }
}
