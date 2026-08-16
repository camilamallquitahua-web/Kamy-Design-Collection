package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Category
import com.example.data.model.GalleryItem
import com.example.data.model.UserCollection
import com.example.data.repository.GalleryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

enum class NavigationTab(val label: String) {
  HOME("Descubrir"),
  EXPLORE("Explorar"),
  SAVED("Guardados"),
  PROFILE("Perfil")
}

data class FilterUiState(
  val currentTab: NavigationTab = NavigationTab.HOME,
  val selectedCategory: Category = Category.ALL,
  val searchQuery: String = "",
  val selectedTag: String? = null,
  val selectedItemId: String? = null,
  val showCreateCollectionDialog: Boolean = false,
  val showAddToCollectionDialog: Boolean = false,
  val selectedItemForCollectionId: String? = null,
  val isDarkThemeForced: Boolean? = null
)

data class GalleryUiState(
  val currentTab: NavigationTab = NavigationTab.HOME,
  val selectedCategory: Category = Category.ALL,
  val searchQuery: String = "",
  val selectedTag: String? = null,
  val selectedItem: GalleryItem? = null,
  val allItems: List<GalleryItem> = emptyList(),
  val filteredItems: List<GalleryItem> = emptyList(),
  val featuredItems: List<GalleryItem> = emptyList(),
  val savedItems: List<GalleryItem> = emptyList(),
  val collections: List<UserCollection> = emptyList(),
  val showCreateCollectionDialog: Boolean = false,
  val showAddToCollectionDialog: Boolean = false,
  val selectedItemForCollection: GalleryItem? = null,
  val isDarkThemeForced: Boolean? = null
)

class GalleryViewModel(
  private val repository: GalleryRepository = GalleryRepository()
) : ViewModel() {

  private val _filterState = MutableStateFlow(FilterUiState())

  val uiState: StateFlow<GalleryUiState> = combine(
    repository.items,
    repository.collections,
    _filterState
  ) { items, collections, filters ->
    val cleanQuery = filters.searchQuery.trim().lowercase()
    val filtered = items.filter { item ->
      val matchesCategory = (filters.selectedCategory == Category.ALL || item.category == filters.selectedCategory)
      val matchesQuery = cleanQuery.isEmpty() ||
        item.title.lowercase().contains(cleanQuery) ||
        item.subtitle.lowercase().contains(cleanQuery) ||
        item.location.lowercase().contains(cleanQuery) ||
        item.tags.any { it.lowercase().contains(cleanQuery) }
      val matchesTag = filters.selectedTag == null || item.tags.contains(filters.selectedTag)
      matchesCategory && matchesQuery && matchesTag
    }

    val currentSelected = filters.selectedItemId?.let { id ->
      items.find { it.id == id }
    }

    val itemForCollection = filters.selectedItemForCollectionId?.let { id ->
      items.find { it.id == id }
    }

    GalleryUiState(
      currentTab = filters.currentTab,
      selectedCategory = filters.selectedCategory,
      searchQuery = filters.searchQuery,
      selectedTag = filters.selectedTag,
      selectedItem = currentSelected,
      allItems = items,
      filteredItems = filtered,
      featuredItems = items.filter { it.isFeatured },
      savedItems = items.filter { it.isSaved },
      collections = collections,
      showCreateCollectionDialog = filters.showCreateCollectionDialog,
      showAddToCollectionDialog = filters.showAddToCollectionDialog,
      selectedItemForCollection = itemForCollection,
      isDarkThemeForced = filters.isDarkThemeForced
    )
  }.stateIn(
    scope = viewModelScope,
    started = SharingStarted.WhileSubscribed(5000),
    initialValue = GalleryUiState()
  )

  fun selectTab(tab: NavigationTab) {
    _filterState.update { it.copy(currentTab = tab) }
  }

  fun selectCategory(category: Category) {
    _filterState.update { it.copy(selectedCategory = category) }
  }

  fun setSearchQuery(query: String) {
    _filterState.update { it.copy(searchQuery = query) }
  }

  fun selectTag(tag: String?) {
    _filterState.update { it.copy(selectedTag = tag) }
  }

  fun openDetail(item: GalleryItem) {
    _filterState.update { it.copy(selectedItemId = item.id) }
  }

  fun closeDetail() {
    _filterState.update { it.copy(selectedItemId = null) }
  }

  fun toggleLike(itemId: String) {
    repository.toggleLike(itemId)
  }

  fun toggleSave(itemId: String) {
    repository.toggleSave(itemId)
  }

  fun addReview(itemId: String, rating: Float, comment: String) {
    repository.addReview(itemId, rating, comment)
  }

  fun showCreateCollectionDialog(show: Boolean) {
    _filterState.update { it.copy(showCreateCollectionDialog = show) }
  }

  fun showAddToCollectionDialog(item: GalleryItem?) {
    _filterState.update {
      it.copy(
        selectedItemForCollectionId = item?.id,
        showAddToCollectionDialog = (item != null)
      )
    }
  }

  fun createCollection(name: String, description: String, coverImageUrl: String) {
    if (name.isNotBlank()) {
      repository.createCollection(name, description, coverImageUrl)
      _filterState.update { it.copy(showCreateCollectionDialog = false) }
    }
  }

  fun addItemToCollection(collectionId: String, itemId: String) {
    repository.addItemToCollection(collectionId, itemId)
    _filterState.update {
      it.copy(
        showAddToCollectionDialog = false,
        selectedItemForCollectionId = null
      )
    }
  }

  fun toggleTheme() {
    _filterState.update { current ->
      val next = when (current.isDarkThemeForced) {
        null -> true
        true -> false
        false -> null
      }
      current.copy(isDarkThemeForced = next)
    }
  }
}
