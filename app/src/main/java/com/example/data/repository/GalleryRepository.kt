package com.example.data.repository

import com.example.data.model.Category
import com.example.data.model.GalleryItem
import com.example.data.model.Review
import com.example.data.model.UserCollection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GalleryRepository {

  private val initialItems: List<GalleryItem> = listOf(
    GalleryItem(
      id = "item-1",
      title = "Santorini al Atardecer",
      subtitle = "Villas blancas y domos azules sobre el mar Egeo",
      category = Category.DESTINATIONS,
      imageUrl = "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?w=800&q=80",
      galleryUrls = listOf(
        "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?w=800&q=80",
        "https://images.unsplash.com/photo-1533105079780-92b9be482077?w=800&q=80",
        "https://images.unsplash.com/photo-1469854523086-cc02fe5d8800?w=800&q=80"
      ),
      authorName = "Elena Vance",
      authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&q=80",
      location = "Oia, Cícladas, Grecia",
      rating = 4.9f,
      reviewsCount = 428,
      likesCount = 1240,
      isLiked = false,
      isSaved = true,
      description = "Descubre las terrazas con vistas panorámicas a la caldera de Santorini. Las puestas de sol tiñen las cúpulas azules y las paredes encaladas de tonos dorados y rosados en una atmósfera inolvidable.",
      tags = listOf("Atardecer", "Mediterráneo", "Arquitectura", "Costero"),
      highlights = listOf("Vistas 360° a la caldera", "Paseos peatonales de piedra", "Catas de vino volcánico"),
      priceTag = "$180 - $450 / noche",
      isFeatured = true,
      userReviews = listOf(
        Review("r1", "Carlos Mendoza", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&q=80", 5f, "¡Una experiencia visual inigualable! Los colores al atardecer son mágicos.", "Hace 2 días"),
        Review("r2", "Lucía Fernández", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&q=80", 5f, "El lugar perfecto para relajarse y tomar fotos espectaculares.", "Hace 1 semana")
      )
    ),
    GalleryItem(
      id = "item-2",
      title = "Café de Especialidad y Repostería",
      subtitle = "Extracción por goteo con granos de origen único",
      category = Category.GASTRONOMY,
      imageUrl = "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=800&q=80",
      galleryUrls = listOf(
        "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=800&q=80",
        "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?w=800&q=80",
        "https://images.unsplash.com/photo-1514432324607-a09d9b4aefdd?w=800&q=80"
      ),
      authorName = "Marco Rossi",
      authorAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150&q=80",
      location = "Florencia, Italia",
      rating = 4.8f,
      reviewsCount = 312,
      likesCount = 890,
      isLiked = true,
      isSaved = false,
      description = "Una propuesta culinaria donde el tostado artesanal se combina con panadería de masa madre fermentada 48 horas. Notas a cacao, frutos secos y jazmín en cada taza.",
      tags = listOf("Café", "Masa Madre", "Artesanal", "Gourmet"),
      highlights = listOf("Granos orgánicos certificados", "Tostado diario in-situ", "Opciones veganas y sin gluten"),
      priceTag = "$4 - $14",
      isFeatured = true,
      userReviews = listOf(
        Review("r3", "Sofía Valdés", "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&q=80", 5f, "El mejor espresso de la ciudad, aroma increíble.", "Hace 3 días")
      )
    ),
    GalleryItem(
      id = "item-3",
      title = "Refugio Nórdico en el Bosque",
      subtitle = "Arquitectura bioclimática de madera y vidrio",
      category = Category.ARCHITECTURE,
      imageUrl = "https://images.unsplash.com/photo-1518780664697-55e3ad937233?w=800&q=80",
      galleryUrls = listOf(
        "https://images.unsplash.com/photo-1518780664697-55e3ad937233?w=800&q=80",
        "https://images.unsplash.com/photo-1448630360428-65456885c650?w=800&q=80",
        "https://images.unsplash.com/photo-1600585154340-be6161a56a0c?w=800&q=80"
      ),
      authorName = "Astrid Lindholm",
      authorAvatar = "https://images.unsplash.com/photo-1544005313-94ddf0286df2?w=150&q=80",
      location = "Bergen, Noruega",
      rating = 4.95f,
      reviewsCount = 189,
      likesCount = 2150,
      isLiked = false,
      isSaved = true,
      description = "Cabaña contemporánea diseñada para fundirse con la vegetación perenne de los fiordos. Iluminación cenital natural, maderas locales tratadas térmicamente y chimenea flotante.",
      tags = listOf("Diseño Nórdico", "Sostenible", "Madera", "Fiordos"),
      highlights = listOf("100% Energía solar y geotérmica", "Sauna privada con vista panorámica", "Acceso directo a rutas de senderismo"),
      priceTag = "$220 / noche",
      isFeatured = true,
      userReviews = listOf(
        Review("r4", "Mateo Ruiz", "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150&q=80", 5f, "Paz absoluta y un diseño impecable en cada rincón.", "Hace 5 días")
      )
    ),
    GalleryItem(
      id = "item-4",
      title = "Bosques Místicos de Kioto",
      subtitle = "Senderos de bambú sagrado y templos milenarios",
      category = Category.NATURE,
      imageUrl = "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=800&q=80",
      galleryUrls = listOf(
        "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=800&q=80",
        "https://images.unsplash.com/photo-1503899036084-c55cdd92da26?w=800&q=80",
        "https://images.unsplash.com/photo-1528164344705-475426879c0d?w=800&q=80"
      ),
      authorName = "Kenji Takahashi",
      authorAvatar = "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?w=150&q=80",
      location = "Arashiyama, Kioto, Japón",
      rating = 4.92f,
      reviewsCount = 680,
      likesCount = 3400,
      isLiked = true,
      isSaved = true,
      description = "Caminar por el bosque de Arashiyama al amanecer es sumergirse en una sinfonía de viento y hojas. Los tallos de bambú se elevan hacia el cielo filtrando suaves rayos de luz.",
      tags = listOf("Bambú", "Zen", "Japón", "Espiritual"),
      highlights = listOf("Horario recomendado: 6:00 AM", "Jardines de té tradicionales", "Templo Tenryu-ji"),
      priceTag = "Entrada libre",
      isFeatured = false,
      userReviews = listOf(
        Review("r5", "Andrea Morales", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&q=80", 5f, "El sonido de las cañas con el viento te renueva por dentro.", "Hace 2 semanas")
      )
    ),
    GalleryItem(
      id = "item-5",
      title = "Taller de Cerámica y Escultura",
      subtitle = "Creación manual con barros naturales y esmaltes orgánicos",
      category = Category.ART_DESIGN,
      imageUrl = "https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?w=800&q=80",
      galleryUrls = listOf(
        "https://images.unsplash.com/photo-1565193566173-7a0ee3dbe261?w=800&q=80",
        "https://images.unsplash.com/photo-1578749556568-bc2c40e68b61?w=800&q=80"
      ),
      authorName = "Camille Laurent",
      authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&q=80",
      location = "Lyon, Francia",
      rating = 4.87f,
      reviewsCount = 94,
      likesCount = 670,
      isLiked = false,
      isSaved = false,
      description = "Espacio creativo enfocado en piezas utilitarias y escultóricas únicas. Cada objeto celebra las imperfecciones de la artesanía manual (wabi-sabi) con acabados mates y texturas terrosas.",
      tags = listOf("Cerámica", "Escultura", "Wabi-Sabi", "Hecho a Mano"),
      highlights = listOf("Talleres intensivos los fines de semana", "Arcillas locales francesas", "Quemas en horno de leña"),
      priceTag = "$45 - $120",
      isFeatured = false,
      userReviews = listOf(
        Review("r6", "Javier Soto", "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?w=150&q=80", 5f, "El taller de fin de semana superó todas mis expectativas.", "Hace 4 días")
      )
    ),
    GalleryItem(
      id = "item-6",
      title = "Pasta Fresca al Tartufo Nero",
      subtitle = "Tagliolini caseros elaborados a mano con trufa fresca",
      category = Category.GASTRONOMY,
      imageUrl = "https://images.unsplash.com/photo-1551183053-bf91a1d81141?w=800&q=80",
      galleryUrls = listOf(
        "https://images.unsplash.com/photo-1551183053-bf91a1d81141?w=800&q=80",
        "https://images.unsplash.com/photo-1621996346565-e3d5d6281691?w=800&q=80"
      ),
      authorName = "Giulia Bertoli",
      authorAvatar = "https://images.unsplash.com/photo-1524504388940-b1c1722653e1?w=150&q=80",
      location = "Bolonia, Italia",
      rating = 4.96f,
      reviewsCount = 510,
      likesCount = 1820,
      isLiked = false,
      isSaved = true,
      description = "Receta centenaria transmitida durante cuatro generaciones. Masa con huevos de granja local, manteca de los Alpes y ralladura generosa de trufa negra de Umbría.",
      tags = listOf("Pasta", "Trufa", "Gourmet", "Tradición"),
      highlights = listOf("Elaboración a la vista", "Maridaje con vinos de la Toscana", "Materia prima con DOP"),
      priceTag = "$26 - $38",
      isFeatured = true,
      userReviews = listOf(
        Review("r7", "Valentina Rios", "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&q=80", 5f, "Una explosión de sabor incomparable. Imprescindible en Bolonia.", "Hace 1 semana")
      )
    ),
    GalleryItem(
      id = "item-7",
      title = "Lago Moraine y Montañas Rocosas",
      subtitle = "Aguas turquesa de origen glaciar rodeadas de diez picos",
      category = Category.NATURE,
      imageUrl = "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&q=80",
      galleryUrls = listOf(
        "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=800&q=80",
        "https://images.unsplash.com/photo-1464822759023-fed622ff2c3b?w=800&q=80"
      ),
      authorName = "Liam Walker",
      authorAvatar = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150&q=80",
      location = "Banff, Alberta, Canadá",
      rating = 4.98f,
      reviewsCount = 1420,
      likesCount = 4890,
      isLiked = true,
      isSaved = true,
      description = "Situado en el Parque Nacional Banff, este lago deslumbra con su intenso color turquesa debido a la refracción de la luz sobre el polvo de roca depositado por los glaciares.",
      tags = listOf("Montañas", "Glaciar", "Kayak", "Aventura"),
      highlights = listOf("Renta de canoas rojas tradicionales", "Mirador Rockpile para fotos épicas", "Senderismo de alta montaña"),
      priceTag = "Pase de Parque $21 CAD",
      isFeatured = false,
      userReviews = listOf(
        Review("r8", "Diego Navarro", "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150&q=80", 5f, "El agua parece pintada a mano. De los lugares más bellos del planeta.", "Hace 3 semanas")
      )
    ),
    GalleryItem(
      id = "item-8",
      title = "Interiorismo Minimalista Japandi",
      subtitle = "Fusión equilibrada de calidez escandinava y serenidad japonesa",
      category = Category.ART_DESIGN,
      imageUrl = "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=800&q=80",
      galleryUrls = listOf(
        "https://images.unsplash.com/photo-1618221195710-dd6b41faaea6?w=800&q=80",
        "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?w=800&q=80"
      ),
      authorName = "Sora Tanaka",
      authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&q=80",
      location = "Copenhague & Tokio",
      rating = 4.89f,
      reviewsCount = 210,
      likesCount = 1450,
      isLiked = false,
      isSaved = false,
      description = "Líneas depuradas, paleta neutra con tonos tierra y textiles de lino natural. Una estética que fomenta la calma, el orden y la conexión con materiales nobles.",
      tags = listOf("Japandi", "Minimalismo", "Decoración", "Luz Natural"),
      highlights = listOf("Mobiliario de roble macizo", "Plantas de bajo mantenimiento", "Iluminación indirecta cálida"),
      priceTag = "Consultoría de diseño",
      isFeatured = false,
      userReviews = listOf(
        Review("r9", "Mariana Gómez", "https://images.unsplash.com/photo-1494790108377-be9c29b29330?w=150&q=80", 5f, "Inspiración pura para rediseñar la sala de estar.", "Hace 1 mes")
      )
    ),
    GalleryItem(
      id = "item-9",
      title = "Estilo Urbano y Moda Sostenible",
      subtitle = "Confección consciente con lino europeo y tintes botánicos",
      category = Category.LIFESTYLE,
      imageUrl = "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=800&q=80",
      galleryUrls = listOf(
        "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=800&q=80",
        "https://images.unsplash.com/photo-1445205170230-053b83016050?w=800&q=80"
      ),
      authorName = "Chloe Martin",
      authorAvatar = "https://images.unsplash.com/photo-1517841905240-472988babdf9?w=150&q=80",
      location = "Barcelona, España",
      rating = 4.81f,
      reviewsCount = 115,
      likesCount = 920,
      isLiked = false,
      isSaved = true,
      description = "Prendas atemporales pensadas para un armario cápsula duradero. Cada silueta equilibra comodidad, elegancia relajada y respeto por los recursos del planeta.",
      tags = listOf("Moda Consciente", "Lino", "Armario Cápsula", "Estilo"),
      highlights = listOf("Cero desperdicio en patronaje", "Trazabilidad completa", "Tejidos transpirables"),
      priceTag = "$65 - $190",
      isFeatured = false,
      userReviews = listOf(
        Review("r10", "Paula Herrero", "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?w=150&q=80", 5f, "La calidad de las telas y el corte son excepcionales.", "Hace 2 semanas")
      )
    )
  )

  private val initialCollections: List<UserCollection> = listOf(
    UserCollection(
      id = "col-1",
      name = "Viajes Soñados",
      description = "Destinos y escapadas para las próximas vacaciones",
      coverImageUrl = "https://images.unsplash.com/photo-1570077188670-e3a8d69ac5ff?w=600&q=80",
      itemIds = listOf("item-1", "item-4", "item-7")
    ),
    UserCollection(
      id = "col-2",
      name = "Gastronomía & Sabores",
      description = "Cafeterías de especialidad y platillos auténticos",
      coverImageUrl = "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=600&q=80",
      itemIds = listOf("item-2", "item-6")
    ),
    UserCollection(
      id = "col-3",
      name = "Inspiración Arquitectura",
      description = "Espacios modernos, madera y diseño contemporáneo",
      coverImageUrl = "https://images.unsplash.com/photo-1518780664697-55e3ad937233?w=600&q=80",
      itemIds = listOf("item-3", "item-8")
    )
  )

  private val _items = MutableStateFlow<List<GalleryItem>>(initialItems)
  val items: StateFlow<List<GalleryItem>> = _items.asStateFlow()

  private val _collections = MutableStateFlow<List<UserCollection>>(initialCollections)
  val collections: StateFlow<List<UserCollection>> = _collections.asStateFlow()

  fun toggleLike(itemId: String) {
    _items.update { list ->
      list.map { item ->
        if (item.id == itemId) {
          val newIsLiked = !item.isLiked
          val delta = if (newIsLiked) 1 else -1
          item.copy(isLiked = newIsLiked, likesCount = (item.likesCount + delta).coerceAtLeast(0))
        } else {
          item
        }
      }
    }
  }

  fun toggleSave(itemId: String) {
    _items.update { list ->
      list.map { item ->
        if (item.id == itemId) {
          item.copy(isSaved = !item.isSaved)
        } else {
          item
        }
      }
    }
  }

  fun addReview(itemId: String, rating: Float, comment: String, userName: String = "Tú") {
    if (comment.isBlank()) return
    val newReview = Review(
      id = "rev_${System.currentTimeMillis()}",
      userName = userName,
      userAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150&q=80",
      rating = rating,
      comment = comment,
      date = "Justo ahora"
    )

    _items.update { list ->
      list.map { item ->
        if (item.id == itemId) {
          val updatedReviews = listOf(newReview) + item.userReviews
          val newCount = item.reviewsCount + 1
          val newRating = ((item.rating * item.reviewsCount) + rating) / newCount
          item.copy(
            userReviews = updatedReviews,
            reviewsCount = newCount,
            rating = (Math.round(newRating * 100.0) / 100.0).toFloat()
          )
        } else {
          item
        }
      }
    }
  }

  fun createCollection(name: String, description: String, coverImageUrl: String) {
    val newCol = UserCollection(
      id = "col_${System.currentTimeMillis()}",
      name = name.trim(),
      description = description.trim(),
      coverImageUrl = if (coverImageUrl.isNotBlank()) coverImageUrl else "https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600&q=80",
      itemIds = emptyList()
    )
    _collections.update { it + newCol }
  }

  fun addItemToCollection(collectionId: String, itemId: String) {
    _collections.update { list ->
      list.map { col ->
        if (col.id == collectionId && !col.itemIds.contains(itemId)) {
          col.copy(itemIds = col.itemIds + itemId)
        } else {
          col
        }
      }
    }
  }
}
