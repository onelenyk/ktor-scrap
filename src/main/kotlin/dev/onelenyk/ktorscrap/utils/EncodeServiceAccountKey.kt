package dev.onelenyk.ktorscrap.utils

import java.io.File
import java.util.Base64

fun main(args: Array<String>) {
    val file = File("/Users/lenyk/projects/Kotlin/onelenykproject/ktor-scrap/src/main/resources/service-account-key.json")

    if (!file.exists()) {
        println("Error: File not found at ${file.path}")
        return
    }

    try {
        val jsonContent = file.readText()
        val encodedString = Base64.getEncoder().encodeToString(jsonContent.toByteArray())
        println(encodedString)
    } catch (e: Exception) {
        println("Error reading or encoding file: ${e.message}")
    }
}
