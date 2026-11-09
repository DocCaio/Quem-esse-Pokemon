package ui

import api.PokemonApi
import model.Pokemon
import util.normalize
import util.scaled
import util.silhouette
import java.awt.Color
import java.awt.Component
import java.awt.Dimension
import java.awt.Font
import javax.swing.Box
import javax.swing.BorderFactory
import javax.swing.BoxLayout
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.SwingConstants
import javax.swing.SwingUtilities
import kotlin.concurrent.thread

class GameWindow : JFrame("Quem é esse Pokémon?") {
    private val imageSize = 300
    private val green = Color(0x2E7D32)
    private val red = Color(0xC62828)

    // Estado do jogo
    private var pokemons = emptyList<Pokemon>()
    private var current: Pokemon? = null
    private var colorIcon: ImageIcon? = null
    private var score = 0
    private var finished = false

    private val titleLabel = JLabel("Quem é esse Pokémon?", SwingConstants.CENTER)
    private val scoreLabel = JLabel("Pontos: 0", SwingConstants.CENTER)
    private val imageLabel = JLabel("Carregando...", SwingConstants.CENTER)
    private val input = JTextField(18)
    private val messageLabel = JLabel(" ", SwingConstants.CENTER)
    private val guessButton = JButton("Chutar")
    private val skipButton = JButton("Desistir")

    init {
        defaultCloseOperation = EXIT_ON_CLOSE
        isResizable = false

        buildLayout()

        input.addActionListener { check() }
        guessButton.addActionListener { check() }
        skipButton.addActionListener { onSkip() }

        setControlsEnabled(false)
        pack()
        setLocationRelativeTo(null)

        thread {
            try {
                pokemons = PokemonApi.fetchPokemonList()
                nextRound()
            } catch (e: Exception) {
                showError("Erro ao buscar os Pokémon. Verifique sua internet.")
            }
        }
    }



    private fun buildLayout() {
        titleLabel.font = Font("SansSerif", Font.BOLD, 24)
        scoreLabel.font = Font("SansSerif", Font.PLAIN, 14)
        messageLabel.font = Font("SansSerif", Font.BOLD, 16)
        imageLabel.preferredSize = Dimension(imageSize, imageSize)
        input.maximumSize = Dimension(280, 30)
        input.horizontalAlignment = JTextField.CENTER

        val buttons = JPanel().apply {
            add(guessButton)
            add(skipButton)
            alignmentX = Component.CENTER_ALIGNMENT
        }

        listOf(titleLabel, scoreLabel, imageLabel, input, messageLabel).forEach {
            it.alignmentX = Component.CENTER_ALIGNMENT
        }

        contentPane = JPanel().apply {
            layout = BoxLayout(this, BoxLayout.Y_AXIS)
            border = BorderFactory.createEmptyBorder(20, 30, 20, 30)
            add(titleLabel)
            add(Box.createVerticalStrut(4))
            add(scoreLabel)
            add(Box.createVerticalStrut(12))
            add(imageLabel)
            add(Box.createVerticalStrut(12))
            add(input)
            add(Box.createVerticalStrut(8))
            add(messageLabel)
            add(Box.createVerticalStrut(8))
            add(buttons)
        }
    }

    private fun setControlsEnabled(enabled: Boolean) {
        input.isEnabled = enabled
        guessButton.isEnabled = enabled
        skipButton.isEnabled = enabled || finished
    }

    private fun showError(text: String) = SwingUtilities.invokeLater {
        imageLabel.text = "Sem imagem"
        messageLabel.foreground = red
        messageLabel.text = text
    }

    // ---------- Lógica do jogo ----------

    private fun nextRound() {
        SwingUtilities.invokeLater {
            finished = false
            input.text = ""
            messageLabel.text = " "
            imageLabel.icon = null
            imageLabel.text = "Carregando..."
            skipButton.text = "Desistir"
            setControlsEnabled(false)
        }
        thread {
            try {
                val pokemon = pokemons.random()
                val image = PokemonApi.fetchImage(pokemon).scaled(imageSize)
                val color = ImageIcon(image)
                val dark = ImageIcon(image.silhouette())
                SwingUtilities.invokeLater {
                    current = pokemon
                    colorIcon = color
                    imageLabel.text = null
                    imageLabel.icon = dark
                    setControlsEnabled(true)
                    input.requestFocusInWindow()
                }
            } catch (e: Exception) {
                showError("Erro ao carregar a imagem. Verifique sua internet.")
            }
        }
    }

    private fun check() {
        val pokemon = current ?: return
        val guess = input.text
        if (finished || guess.isBlank()) return

        if (normalize(guess) == normalize(pokemon.name)) {
            score++
            scoreLabel.text = "Pontos: $score"
            messageLabel.foreground = green
            messageLabel.text = "Acertou! É o ${pokemon.displayName}! 🎉"
            finish()
        } else {
            messageLabel.foreground = red
            messageLabel.text = "Tente novamente!"
            input.selectAll()
            input.requestFocusInWindow()
        }
    }

    private fun onSkip() {
        if (finished) {
            nextRound()
        } else {
            messageLabel.foreground = red
            messageLabel.text = "Era o ${current?.displayName}!"
            finish()
        }
    }


    private fun finish() {
        finished = true
        imageLabel.icon = colorIcon
        input.isEnabled = false
        guessButton.isEnabled = false
        skipButton.text = "Próximo"
        skipButton.isEnabled = true
        skipButton.requestFocusInWindow()
    }
}