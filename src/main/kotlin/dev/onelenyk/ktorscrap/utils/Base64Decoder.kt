package dev.onelenyk.ktorscrap.utils

object Base64Decoder {
    fun decode(base64String: String): String {
        return String(java.util.Base64.getDecoder().decode(base64String))
    }
}
