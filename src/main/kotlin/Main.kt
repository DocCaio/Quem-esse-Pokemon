package pokemon


import ui.GameWindow
import javax.swing.SwingUtilities

fun main() {
    SwingUtilities.invokeLater { GameWindow().isVisible = true }
}