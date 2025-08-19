package dev.onelenyk.ktorscrap.data.db

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import dev.onelenyk.ktorscrap.presentation.env.DbCredentials

class FirestoreDatabase(private val env: DbCredentials) {
    val db: Firestore by lazy {
        val serviceAccount = this::class.java.classLoader.getResourceAsStream("service-account-key.json")
        val credentials = GoogleCredentials.fromStream(serviceAccount)
        val options =
            FirebaseOptions.builder()
                .setCredentials(credentials)
                .setProjectId(env.firestoreProjectId)
                .build()
        FirebaseApp.initializeApp(options)
        FirestoreClient.getFirestore()
    }
}
