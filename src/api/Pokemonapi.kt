package pokemon.api

import pokemon.model.Pokemon
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

object PokemonApi {
    private const val LIST_URL = "https://pokeapi.co/api/v2/pokemon?limit=151"
    private val ENTRY_REGEX = Regex("\"name\":\"([^\"]+)\",\"url\":\"[^\"]+/pokemon/(\\d+)/\"")


    fun fetchPokemonList(): List<Pokemon> {
        val json = URL(LIST_URL).readText()
        return ENTRY_REGEX.findAll(json)
            .map { Pokemon(id = it.groupValues[2].toInt(), name = it.groupValues[1]) }
            .toList()
    }


    fun fetchImage(pokemon: Pokemon): BufferedImage = ImageIO.read(URL(pokemon.imageUrl))
}