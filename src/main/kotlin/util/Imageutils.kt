package util

import java.awt.RenderingHints
import java.awt.image.BufferedImage


fun BufferedImage.scaled(size: Int): BufferedImage {
    val out = BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB)
    val g = out.createGraphics()
    g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR)
    g.drawImage(this, 0, 0, size, size, null)
    g.dispose()
    return out
}


fun BufferedImage.silhouette(): BufferedImage {
    val out = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    for (y in 0 until height) {
        for (x in 0 until width) {
            out.setRGB(x, y, getRGB(x, y) and 0xFF000000.toInt())
        }
    }
    return out
}