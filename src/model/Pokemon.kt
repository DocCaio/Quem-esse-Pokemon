package model

data class Pokemon(val id: Int, val name: String) {
    val imageUrl: String
        get() = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"


    val displayName: String
        get() = name.split("-").joinToString(" ") { it.replaceFirstChar(Char::uppercase) }
}