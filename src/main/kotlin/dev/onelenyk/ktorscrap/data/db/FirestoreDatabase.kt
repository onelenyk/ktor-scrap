package dev.onelenyk.ktorscrap.data.db

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.cloud.FirestoreClient
import dev.onelenyk.ktorscrap.presentation.env.DbCredentials
import dev.onelenyk.ktorscrap.presentation.env.EnvironmentManager
import java.io.ByteArrayInputStream

class FirestoreDatabase(private val env: DbCredentials) {
    val db: Firestore by lazy {
        initializeFirebaseApp()
        FirestoreClient.getFirestore()
    }

    private fun getServiceAccountKeyStream() =
        ByteArrayInputStream(EnvironmentManager.getGcpServiceAccountKey().toByteArray())


    private fun buildFirebaseOptions(credentials: GoogleCredentials): FirebaseOptions =
        FirebaseOptions.builder()
            .setCredentials(credentials)
            .setProjectId(env.firestoreProjectId)
            .build()

    private fun initializeFirebaseApp() {
        if (FirebaseApp.getApps().isEmpty()) {
            val serviceAccountStream = getServiceAccountKeyStream()
            val credentials = GoogleCredentials.fromStream(serviceAccountStream)
            val options = buildFirebaseOptions(credentials)
            FirebaseApp.initializeApp(options)
        }
    }
}
