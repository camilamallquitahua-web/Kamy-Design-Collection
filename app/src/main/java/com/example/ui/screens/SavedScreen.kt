package com.example.ui.screens

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CollectionsBookmark
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import com.example.data.model.UserCollection
import com.example.ui.components.EmptyStateView
import com.example.ui.components.ItemSavedCard
import com.example.ui.viewmodel.GalleryUiState
import com.example.ui.viewmodel.GalleryViewModel

@Composable
fun SavedScreen(
  uiState: GalleryUiState,
  viewModel: GalleryViewModel,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("saved_screen_list"),
    contentPadding = PaddingValues(bottom = 90.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Collections section header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column {
          Text(
            text = "Tus Colecciones",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
          )
          Text(
            text = "${uiState.collections.size} carpetas temáticas",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }

        Button(
          onClick = { viewModel.showCreateCollectionDialog(true) },
          colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
          shape = RoundedCornerShape(12.dp),
          modifier = Modifier.testTag("create_collection_btn")
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Nueva", style = MaterialTheme.typography.labelSmall)
        }
      }
    }

    // Collections horizontal list
    item {
      LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        items(uiState.collections, key = { it.id }) { collection ->
          CollectionCard(
            collection = collection,
            onClick = {
              // Filter or view collection items
            }
          )
        }
      }
    }

    // Saved Items Header
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = "Todos los Guardados",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground
        )

        Text(
          text = "${uiState.savedItems.size} elementos",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Saved items
    if (uiState.savedItems.isEmpty()) {
      item {
        EmptyStateView(
          icon = Icons.Default.BookmarkBorder,
          title = "Aún no tienes elementos guardados",
          description = "Toca el ícono del marcador en cualquier fotografía o destino para guardarlo aquí y acceder rápido.",
          actionButtonText = "Explorar Contenido",
          onActionClick = {
            viewModel.selectTab(com.example.ui.viewmodel.NavigationTab.HOME)
          }
        )
      }
    } else {
      items(uiState.savedItems, key = { it.id }) { item ->
        ItemSavedCard(
          item = item,
          onClick = { viewModel.openDetail(item) },
          onRemoveSave = { viewModel.toggleSave(item.id) },
          modifier = Modifier.padding(horizontal = 16.dp)
        )
      }
    }
  }
}

@Composable
fun CollectionCard(
  collection: UserCollection,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Card(
    modifier = modifier
      .width(180.dp)
      .height(130.dp)
      .clip(RoundedCornerShape(16.dp))
      .clickable(onClick = onClick),
    shape = RoundedCornerShape(16.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
  ) {
    Box(modifier = Modifier.fillMaxSize()) {
      AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
          .data(collection.coverImageUrl)
          .crossfade(true)
          .build(),
        contentDescription = collection.name,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
      )

      Box(
        modifier = Modifier
          .fillMaxSize()
          .clip(RoundedCornerShape(16.dp))
      )

      // Gradient overlay
      Surface(
        color = Color.Black.copy(alpha = 0.45f),
        modifier = Modifier.fillMaxSize()
      ) {}

      Column(
        modifier = Modifier
          .align(Alignment.BottomStart)
          .padding(10.dp)
      ) {
        Text(
          text = collection.name,
          color = Color.White,
          style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
          ),
          maxLines = 1,
          overflow = TextOverflow.Ellipsis
        )
        Text(
          text = "${collection.itemIds.size} guardados",
          color = Color.White.copy(alpha = 0.8f),
          style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp)
        )
      }
    }
  }
}
