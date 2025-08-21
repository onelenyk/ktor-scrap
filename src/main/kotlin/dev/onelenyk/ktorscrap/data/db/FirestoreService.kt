package dev.onelenyk.ktorscrap.data.db

import com.google.cloud.firestore.Firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FirestoreService<T : Identifiable>(
    private val firestore: Firestore,
    private val collectionName: String,
    private val clazz: Class<T>,
) : Database<T> {
    private val collection = firestore.collection(collectionName)

    override suspend fun getAll(): List<T> =
        withContext(Dispatchers.IO) {
            val snapshot = collection.get().get()
            snapshot.toObjects(clazz)
        }

    override suspend fun getById(id: String): T? =
        withContext(Dispatchers.IO) {
            val snapshot = collection.document(id).get().get()
            snapshot.toObject(clazz)
        }

    override suspend fun create(item: T): T =
        withContext(Dispatchers.IO) {
            collection.document(item.id).set(item).get()
            item
        }

    override suspend fun update(item: T): T =
        withContext(Dispatchers.IO) {
            collection.document(item.id).set(item).get()
            item
        }

    override suspend fun delete(id: String): Boolean {
        return withContext(Dispatchers.IO) {
            collection.document(id).delete().get()
            true
        }
    }
}
