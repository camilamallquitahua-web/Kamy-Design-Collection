package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AddToCollectionDialog
import com.example.ui.components.AppHeader
import com.example.ui.components.CreateCollectionDialog
import com.example.ui.screens.DetailScreen
import com.example.ui.screens.ExploreScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SavedScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.GalleryViewModel
import com.example.ui.viewmodel.NavigationTab

@Composable
fun MainApp(
  viewModel: GalleryViewModel = viewModel()
) {
  val uiState by viewModel.uiState.collectAsStateWithLifecycle()
  val systemDark = isSystemInDarkTheme()
  val isDarkTheme = uiState.isDarkThemeForced ?: systemDark

  MyApplicationTheme(darkTheme = isDarkTheme) {
    Box(modifier = Modifier.fillMaxSize()) {
      Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
          if (uiState.selectedItem == null) {
            val title = when (uiState.currentTab) {
              NavigationTab.HOME -> "Galería & Descubre"
              NavigationTab.EXPLORE -> "Mosaico Visual"
              NavigationTab.SAVED -> "Tus Guardados"
              NavigationTab.PROFILE -> "Mi Perfil"
            }
            val subtitle = when (uiState.currentTab) {
              NavigationTab.HOME -> "Fotografía, destinos & gastronomía"
              NavigationTab.EXPLORE -> "Inspiración en cuadrícula"
              NavigationTab.SAVED -> "Colecciones y favoritos organizados"
              NavigationTab.PROFILE -> "Estadísticas y configuración"
            }

            AppHeader(
              title = title,
              subtitle = subtitle,
              searchQuery = uiState.searchQuery,
              onSearchQueryChange = { viewModel.setSearchQuery(it) },
              onToggleTheme = { viewModel.toggleTheme() },
              isDarkTheme = isDarkTheme,
              showSearch = (uiState.currentTab == NavigationTab.HOME || uiState.currentTab == NavigationTab.EXPLORE)
            )
          }
        },
        bottomBar = {
          if (uiState.selectedItem == null) {
            NavigationBar(
              modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .testTag("main_navigation_bar"),
              tonalElevation = 8.dp
            ) {
              NavigationTab.entries.forEach { tab ->
                val isSelected = uiState.currentTab == tab
                val (selectedIcon, unselectedIcon) = when (tab) {
                  NavigationTab.HOME -> Icons.Filled.Home to Icons.Outlined.Home
                  NavigationTab.EXPLORE -> Icons.Filled.Explore to Icons.Outlined.Explore
                  NavigationTab.SAVED -> Icons.Filled.Bookmark to Icons.Filled.BookmarkBorder
                  NavigationTab.PROFILE -> Icons.Filled.Person to Icons.Outlined.Person
                }

                NavigationBarItem(
                  selected = isSelected,
                  onClick = { viewModel.selectTab(tab) },
                  icon = {
                    Icon(
                      imageVector = if (isSelected) selectedIcon else unselectedIcon,
                      contentDescription = tab.label
                    )
                  },
                  label = { Text(text = tab.label) },
                  modifier = Modifier.testTag("nav_tab_${tab.name.lowercase()}")
                )
              }
            }
          }
        }
      ) { innerPadding ->
        AnimatedContent(
          targetState = uiState.currentTab,
          transitionSpec = { fadeIn() togetherWith fadeOut() },
          modifier = Modifier.padding(innerPadding),
          label = "TabTransition"
        ) { tab ->
          when (tab) {
            NavigationTab.HOME -> HomeScreen(uiState = uiState, viewModel = viewModel)
            NavigationTab.EXPLORE -> ExploreScreen(uiState = uiState, viewModel = viewModel)
            NavigationTab.SAVED -> SavedScreen(uiState = uiState, viewModel = viewModel)
            NavigationTab.PROFILE -> ProfileScreen(uiState = uiState, viewModel = viewModel, isDarkTheme = isDarkTheme)
          }
        }
      }

      // Fullscreen Item Detail Sheet Overlay
      AnimatedVisibility(
        visible = uiState.selectedItem != null,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
      ) {
        uiState.selectedItem?.let { item ->
          BackHandler { viewModel.closeDetail() }
          DetailScreen(
            item = item,
            viewModel = viewModel,
            onBack = { viewModel.closeDetail() }
          )
        }
      }

      // Dialog: Create collection
      if (uiState.showCreateCollectionDialog) {
        CreateCollectionDialog(
          onDismiss = { viewModel.showCreateCollectionDialog(false) },
          onConfirm = { name, desc, cover ->
            viewModel.createCollection(name, desc, cover)
          }
        )
      }

      // Dialog: Add to collection
      if (uiState.showAddToCollectionDialog && uiState.selectedItemForCollection != null) {
        AddToCollectionDialog(
          item = uiState.selectedItemForCollection!!,
          collections = uiState.collections,
          onDismiss = { viewModel.showAddToCollectionDialog(null) },
          onSelectCollection = { colId ->
            viewModel.addItemToCollection(colId, uiState.selectedItemForCollection!!.id)
          },
          onCreateNewCollection = {
            viewModel.showCreateCollectionDialog(true)
          }
        )
      }
    }
  }
}
