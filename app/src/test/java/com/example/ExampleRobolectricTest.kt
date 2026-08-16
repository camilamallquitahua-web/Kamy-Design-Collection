package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.GalleryRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Galería", appName)
  }

  @Test
  fun `repository contains initial items and toggle like works`() {
    val repository = GalleryRepository()
    val initialItems = repository.items.value
    assertTrue(initialItems.isNotEmpty())

    val firstItem = initialItems.first()
    val initialLike = firstItem.isLiked
    repository.toggleLike(firstItem.id)

    val updatedItem = repository.items.value.first { it.id == firstItem.id }
    assertEquals(!initialLike, updatedItem.isLiked)
  }
}

