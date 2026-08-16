package com.example.ui.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.LocalDining
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.data.model.Category

@Composable
fun CategoryChips(
  selectedCategory: Category,
  onCategorySelected: (Category) -> Unit,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier
      .horizontalScroll(rememberScrollState())
      .padding(horizontal = 16.dp, vertical = 6.dp),
    horizontalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Category.entries.forEach { category ->
      val isSelected = category == selectedCategory
      val icon = when (category) {
        Category.ALL -> Icons.Default.AutoAwesome
        Category.DESTINATIONS -> Icons.Default.Flight
        Category.GASTRONOMY -> Icons.Default.LocalDining
        Category.ARCHITECTURE -> Icons.Default.Apartment
        Category.NATURE -> Icons.Default.Grass
        Category.ART_DESIGN -> Icons.Default.Palette
        Category.LIFESTYLE -> Icons.Default.Style
      }

      FilterChip(
        selected = isSelected,
        onClick = { onCategorySelected(category) },
        label = {
          Text(
            text = category.displayName,
            style = MaterialTheme.typography.labelMedium
          )
        },
        leadingIcon = {
          Icon(
            imageVector = if (isSelected) Icons.Default.Check else icon,
            contentDescription = category.displayName,
            modifier = Modifier.size(16.dp)
          )
        },
        colors = FilterChipDefaults.filterChipColors(
          selectedContainerColor = MaterialTheme.colorScheme.primary,
          selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
          selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
          containerColor = MaterialTheme.colorScheme.surfaceVariant,
          labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        modifier = Modifier.testTag("category_chip_${category.name.lowercase()}")
      )
    }
  }
}
