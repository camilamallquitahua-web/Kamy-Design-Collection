package com.example.data.model

enum class Category(val displayName: String, val iconName: String) {
  ALL("Todo", "grid"),
  DESTINATIONS("Destinos", "flight"),
  GASTRONOMY("Gastronomía", "restaurant"),
  ARCHITECTURE("Arquitectura", "apartment"),
  NATURE("Naturaleza", "park"),
  ART_DESIGN("Arte y Diseño", "palette"),
  LIFESTYLE("Estilo de Vida", "style")
}

data class Review(
  val id: String,
  val userName: String,
  val userAvatar: String,
  val rating: Float,
  val comment: String,
  val date: String
)

data class GalleryItem(
  val id: String,
  val title: String,
  val subtitle: String,
  val category: Category,
  val imageUrl: String,
  val galleryUrls: List<String> = emptyList(),
  val authorName: String,
  val authorAvatar: String,
  val location: String,
  val rating: Float,
  val reviewsCount: Int,
  val likesCount: Int,
  val isLiked: Boolean = false,
  val isSaved: Boolean = false,
  val description: String,
  val tags: List<String>,
  val highlights: List<String> = emptyList(),
  val priceTag: String? = null,
  val isFeatured: Boolean = false,
  val userReviews: List<Review> = emptyList()
)

data class UserCollection(
  val id: String,
  val name: String,
  val description: String,
  val coverImageUrl: String,
  val itemIds: List<String> = emptyList()
)
