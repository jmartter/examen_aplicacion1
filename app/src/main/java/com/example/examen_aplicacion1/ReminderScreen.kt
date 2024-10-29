package com.example.examen_aplicacion1

import android.view.ContextMenu
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.res.stringResource
import com.google.firebase.firestore.FirebaseFirestore
import com.example.examen_aplicacion1.ui.theme.Examen_aplicacion1Theme

@Composable
fun ReminderApp(db: FirebaseFirestore, initialReminders: List<Reminder>) {
    var reminderText by remember { mutableStateOf("") }
    var recordatorios by remember { mutableStateOf(initialReminders) }
    var selectedReminder by remember { mutableStateOf<Reminder?>(null) }
    var showMenu by remember { mutableStateOf(false) }
    var showDone by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.my_reminders),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(top = 32.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                TextField(
                    value = reminderText,
                    onValueChange = { reminderText = it },
                    label = { Text(stringResource(id = R.string.new_reminder)) },
                    modifier = Modifier.weight(1f)
                )
                IconButton(
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
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = stringResource(id = R.string.add))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn {
                items(recordatorios.filter { it.isDone == showDone }) { reminder ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .background(if (reminder == selectedReminder) Color.LightGray else Color.Transparent)
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
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = { showDone = false }) {
                Text(stringResource(id = R.string.pending))
            }
            Button(onClick = { showDone = true }) {
                Text(stringResource(id = R.string.done))
            }
        }
    }

    DropdownMenu(
        expanded = showMenu,
        onDismissRequest = { showMenu = false }
    ) {
        DropdownMenuItem(
            text = { Text("Eliminar") },
            onClick = {
                // Acción de eliminar
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
            }
        )
        DropdownMenuItem(
            text = { Text("Marcar como hecho") },
            onClick = {
                // Acción de marcar como hecho
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
