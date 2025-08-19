package dev.onelenyk.ktorscrap.data.db

interface Database<T : Identifiable> {
    suspend fun getAll(): List<T>
    suspend fun getById(id: String): T?
    suspend fun create(item: T): T
    suspend fun update(item: T): T
    suspend fun delete(id: String)
}
