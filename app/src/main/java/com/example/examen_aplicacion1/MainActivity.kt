package com.example.examen_aplicacion1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.examen_aplicacion1.ui.theme.Examen_aplicacion1Theme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        setContent {
            Examen_aplicacion1Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReminderApp(db)
                }
            }
        }
    }
}

@Composable
fun ReminderApp(db: FirebaseFirestore) {
    var reminderText by remember { mutableStateOf("") }
    var recordatorios by remember { mutableStateOf(listOf<Reminder>()) }
    var selectedReminder by remember { mutableStateOf<Reminder?>(null) }
    var showMenu by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Mis Recordatorios", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = reminderText,
            onValueChange = { reminderText = it },
            label = { Text("Nuevo Recordatorio") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                if (reminderText.isNotEmpty()) {
                    val newReminder = Reminder(reminderText)
                    recordatorios = recordatorios + newReminder
                    reminderText = ""
                    db.collection("recordatorios")
                        .add(newReminder)
                        .addOnSuccessListener { documentReference ->
                            // Éxito al agregar el recordatorio
                        }
                        .addOnFailureListener { e ->
                            // Error al agregar el recordatorio
                        }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Añadir")
        }
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            items(recordatorios.filter { !it.isDone }) { reminder ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            selectedReminder = reminder
                            showMenu = true
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = reminder.text, style = MaterialTheme.typography.bodyLarge)
                }
                Divider()
            }
        }
    }

    if (showMenu && selectedReminder != null) {
        ContextMenu(
            reminder = selectedReminder!!,
            onDismiss = { showMenu = false },
            onDelete = {
                recordatorios = recordatorios.filter { it != selectedReminder }
                db.collection("recordatorios")
                    .whereEqualTo("text", selectedReminder!!.text)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("recordatorios").document(document.id).delete()
                        }
                    }
                showMenu = false
            },
            onMarkAsDone = {
                val updatedReminder = selectedReminder!!.copy(isDone = true)
                recordatorios = recordatorios.map {
                    if (it == selectedReminder) updatedReminder else it
                }
                db.collection("recordatorios")
                    .whereEqualTo("text", selectedReminder!!.text)
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            db.collection("recordatorios").document(document.id)
                                .update("isDone", true)
                        }
                    }
                showMenu = false
            }
        )
    }
}

@Composable
fun ContextMenu(
    reminder: Reminder,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onMarkAsDone: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Opciones") },
        text = { Text(text = "Selecciona una opción para el recordatorio") },
        confirmButton = {
            Column {
                TextButton(onClick = onDelete) {
                    Text("Eliminar")
                }
                TextButton(onClick = onMarkAsDone) {
                    Text("Hecho")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Examen_aplicacion1Theme {
        ReminderApp(FirebaseFirestore.getInstance())
    }
}