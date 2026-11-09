package util


fun normalize(text: String): String = text.lowercase().filter { it.isLetterOrDigit() }