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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Examen_aplicacion1Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ReminderApp()
                }
            }
        }
    }
}

@Composable
fun ReminderApp() {
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
                    reminders = reminders + Reminder(reminderText)
                    reminderText = ""
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
        ReminderApp()
    }
}