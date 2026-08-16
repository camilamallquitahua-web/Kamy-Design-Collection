package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.GalleryItem
import com.example.data.model.UserCollection

@Composable
fun CreateCollectionDialog(
  onDismiss: () -> Unit,
  onConfirm: (name: String, description: String, coverUrl: String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var description by remember { mutableStateOf("") }
  var coverUrl by remember { mutableStateOf("https://images.unsplash.com/photo-1506744038136-46273834b3fb?w=600&q=80") }

  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Nueva Colección",
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
      ) {
        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Nombre de la colección") },
          placeholder = { Text("Ej. Lugares por visitar, Cafeterías...") },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("collection_name_input")
        )

        OutlinedTextField(
          value = description,
          onValueChange = { description = it },
          label = { Text("Descripción (opcional)") },
          placeholder = { Text("Breve resumen de esta colección") },
          maxLines = 2,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("collection_desc_input")
        )

        OutlinedTextField(
          value = coverUrl,
          onValueChange = { coverUrl = it },
          label = { Text("URL de Portada (Imagen)") },
          singleLine = true,
          modifier = Modifier.fillMaxWidth()
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirm(name, description, coverUrl) },
        enabled = name.isNotBlank(),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        modifier = Modifier.testTag("confirm_create_collection_button")
      ) {
        Text("Crear")
      }
    },
    dismissButton = {
      TextButton(onClick = onDismiss) {
        Text("Cancelar")
      }
    }
  )
}

@Composable
fun AddToCollectionDialog(
  item: GalleryItem,
  collections: List<UserCollection>,
  onDismiss: () -> Unit,
  onSelectCollection: (collectionId: String) -> Unit,
  onCreateNewCollection: () -> Unit
) {
  AlertDialog(
    onDismissRequest = onDismiss,
    title = {
      Text(
        text = "Guardar en Colección",
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
      )
    },
    text = {
      Column {
        Text(
          text = "Selecciona dónde agregar \"${item.title}\":",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
          verticalArrangement = Arrangement.spacedBy(8.dp),
          modifier = Modifier.height(200.dp)
        ) {
          items(collections, key = { it.id }) { col ->
            val containsItem = col.itemIds.contains(item.id)
            Card(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable { onSelectCollection(col.id) },
              colors = CardDefaults.cardColors(
                containerColor = if (containsItem) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant
              )
            ) {
              Row(
                modifier = Modifier
                  .fillMaxWidth()
                  .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
              ) {
                AsyncImage(
                  model = ImageRequest.Builder(LocalContext.current)
                    .data(col.coverImageUrl)
                    .crossfade(true)
                    .build(),
                  contentDescription = null,
                  modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp)),
                  contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = col.name,
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                  )
                  Text(
                    text = "${col.itemIds.size} elementos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                  )
                }

                if (containsItem) {
                  Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Guardado",
                    tint = MaterialTheme.colorScheme.primary
                  )
                }
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(
          onClick = {
            onDismiss()
            onCreateNewCollection()
          },
          modifier = Modifier.fillMaxWidth()
        ) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(6.dp))
          Text("Crear nueva colección")
        }
      }
    },
    confirmButton = {
      TextButton(onClick = onDismiss) {
        Text("Cerrar")
      }
    }
  )
}
