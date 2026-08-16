package com.example.ui.screens

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridOn
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.components.CategoryChips
import com.example.ui.components.EmptyStateView
import com.example.ui.components.ItemGridCard
import com.example.ui.viewmodel.GalleryUiState
import com.example.ui.viewmodel.GalleryViewModel

@Composable
fun ExploreScreen(
  uiState: GalleryUiState,
  viewModel: GalleryViewModel,
  modifier: Modifier = Modifier
) {
  val popularTags = listOf("Atardecer", "Gourmet", "Diseño Nórdico", "Bambú", "Cerámica", "Montañas", "Japandi", "Moda Consciente")

  LazyVerticalGrid(
    columns = GridCells.Fixed(2),
    modifier = modifier
      .fillMaxSize()
      .testTag("explore_grid"),
    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 90.dp),
    horizontalArrangement = Arrangement.spacedBy(12.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    // Categories
    item(span = { GridItemSpan(2) }) {
      Column {
        CategoryChips(
          selectedCategory = uiState.selectedCategory,
          onCategorySelected = { viewModel.selectCategory(it) }
        )

        // Popular tag pills
        Row(
          modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(vertical = 4.dp),
          horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          popularTags.forEach { tag ->
            val isSelected = uiState.selectedTag == tag
            FilterChip(
              selected = isSelected,
              onClick = {
                if (isSelected) viewModel.selectTag(null) else viewModel.selectTag(tag)
              },
              label = { Text("#$tag") },
              colors = FilterChipDefaults.filterChipColors(
                selectedContainerColor = MaterialTheme.colorScheme.secondary,
                selectedLabelColor = MaterialTheme.colorScheme.onSecondary
              )
            )
          }
        }
      }
    }

    // Grid Count Header
    item(span = { GridItemSpan(2) }) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(
          text = if (uiState.selectedTag != null) "Etiqueta: #${uiState.selectedTag}" else "Mosaico Visual",
          style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
          color = MaterialTheme.colorScheme.onBackground
        )
        Text(
          text = "${uiState.filteredItems.size} fotos",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
      }
    }

    if (uiState.filteredItems.isEmpty()) {
      item(span = { GridItemSpan(2) }) {
        EmptyStateView(
          title = "No se encontraron elementos",
          description = "Prueba con otra categoría o elimina los filtros activos.",
          actionButtonText = "Ver Todo",
          onActionClick = {
            viewModel.selectCategory(com.example.data.model.Category.ALL)
            viewModel.selectTag(null)
          }
        )
      }
    } else {
      items(uiState.filteredItems, key = { it.id }) { item ->
        ItemGridCard(
          item = item,
          onClick = { viewModel.openDetail(item) },
          onToggleLike = { viewModel.toggleLike(item.id) }
        )
      }
    }
  }
}
