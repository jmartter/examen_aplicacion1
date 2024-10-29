package com.example.examen_aplicacion1

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.examen_aplicacion1.ui.theme.Examen_aplicacion1Theme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()

        // Fetch data from Firebase Firestore
        db.collection("recordatorios")
            .get()
            .addOnSuccessListener { result: QuerySnapshot ->
                val reminders = result.documents.map { document ->
                    Reminder(
                        text = document.getString("text") ?: "",
                        isDone = document.getBoolean("isDone") ?: false
                    )
                }
                setContent {
                    Examen_aplicacion1Theme {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Column {
                                LanguageToggleButton()
                                ReminderApp(db, reminders)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }

    @Composable
    fun LanguageToggleButton() {
        var currentLocale = resources.configuration.locales[0]
        val newLocale = if (currentLocale.language == "en") Locale("es") else Locale("en")

        Button(onClick = {
            val config = Configuration(resources.configuration)
            config.setLocale(newLocale)
            resources.updateConfiguration(config, resources.displayMetrics)
            recreate() // Restart activity to apply changes
        }) {
            Text(text = getString(R.string.change_language))
        }
    }
}