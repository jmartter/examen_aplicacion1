package com.example.examen_aplicacion1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import com.example.examen_aplicacion1.ui.theme.Examen_aplicacion1Theme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

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
                            ReminderApp(db, reminders)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error
            }
    }
}