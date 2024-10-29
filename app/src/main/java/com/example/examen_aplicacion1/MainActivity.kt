package com.example.examen_aplicacion1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
    var reminders by remember { mutableStateOf(listOf<Reminder>()) }

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
                    reminders = reminders + newReminder
                    reminderText = ""
                    db.collection("reminders")
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
            items(reminders) { reminder ->
                Text(text = reminder.text, style = MaterialTheme.typography.bodyLarge)
                Divider()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Examen_aplicacion1Theme {
        ReminderApp(FirebaseFirestore.getInstance())
    }
}