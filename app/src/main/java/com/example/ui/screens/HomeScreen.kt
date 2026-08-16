package com.example.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.GalleryItem
import com.example.ui.components.CategoryChips
import com.example.ui.components.EmptyStateView
import com.example.ui.components.HeroCarousel
import com.example.ui.components.ItemDiscoveryCard
import com.example.ui.viewmodel.GalleryUiState
import com.example.ui.viewmodel.GalleryViewModel

@Composable
fun HomeScreen(
  uiState: GalleryUiState,
  viewModel: GalleryViewModel,
  modifier: Modifier = Modifier
) {
  LazyColumn(
    modifier = modifier
      .fillMaxSize()
      .testTag("home_screen_list"),
    contentPadding = PaddingValues(bottom = 90.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // Hero Carousel (Only shown when not searching)
    if (uiState.searchQuery.isEmpty() && uiState.featuredItems.isNotEmpty()) {
      item {
        HeroCarousel(
          items = uiState.featuredItems,
          onItemClick = { viewModel.openDetail(it) },
          onToggleLike = { viewModel.toggleLike(it) },
          onToggleSave = { viewModel.toggleSave(it) }
        )
      }
    }

    // Category Selector
    item {
      Column {
        Text(
          text = "Explorar Categorías",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground,
          modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )
        CategoryChips(
          selectedCategory = uiState.selectedCategory,
          onCategorySelected = { viewModel.selectCategory(it) }
        )
      }
    }

    // Header for feed
    item {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (uiState.searchQuery.isNotEmpty()) "Resultados de búsqueda" else "Recomendaciones para ti",
          style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground
        )

        Text(
          text = "${uiState.filteredItems.size} lugares",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    // Filtered Feed Items
    if (uiState.filteredItems.isEmpty()) {
      item {
        EmptyStateView(
          title = "Sin resultados encontrados",
          description = "No encontramos elementos para \"${uiState.searchQuery}\". Intenta con otra palabra clave.",
          actionButtonText = "Limpiar búsqueda",
          onActionClick = {
            viewModel.setSearchQuery("")
            viewModel.selectCategory(com.example.data.model.Category.ALL)
          }
        )
      }
    } else {
      items(uiState.filteredItems, key = { it.id }) { item ->
        ItemDiscoveryCard(
          item = item,
          onClick = { viewModel.openDetail(item) },
          onToggleLike = { viewModel.toggleLike(item.id) },
          onToggleSave = { viewModel.toggleSave(item.id) },
          onAddToCollection = { viewModel.showAddToCollectionDialog(item) },
          modifier = Modifier.padding(horizontal = 16.dp)
        )
      }
    }
  }
}
