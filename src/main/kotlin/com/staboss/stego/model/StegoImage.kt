package com.staboss.stego.model

import com.staboss.stego.util.requireOrExit
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * [StegoImage] contains all the necessary data and information about stego-image-container,
 * which can be changed during the operation of the stego-method
 */
open class StegoImage(imageFile: File) {

    private val image: BufferedImage

    val width: Int
    val height: Int
    val capacity: Int

    val red: Array<IntArray>
    val green: Array<IntArray>
    val blue: Array<IntArray>

    init {
        requireOrExit(imageFile.exists()) {
            "Image does not exist : ${imageFile.absolutePath}"
        }

        image = ImageIO.read(imageFile)

        width = image.width
        height = image.height
        capacity = width * height

        red = Array(height) { IntArray(width) }
        green = Array(height) { IntArray(width) }
        blue = Array(height) { IntArray(width) }

        var pixel: Color
        for (i in 0 until height) {
            for (j in 0 until width) {
                pixel = Color(image.getRGB(j, i))
                red[i][j] = pixel.red
                green[i][j] = pixel.green
                blue[i][j] = pixel.blue
            }
        }
    }

    /**
     * Writes new RGB values in the [newImage] file
     *
     * @param newImage Image File
     * @param newRed red channel values
     * @param newGreen green channel values
     * @param newBlue blue channel values
     */
    protected fun writeImage(
        newImage: File,
        newRed: Array<IntArray> = red,
        newGreen: Array<IntArray> = green,
        newBlue: Array<IntArray> = blue
    ) {
        val bufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)

        var color: Color
        for (i in 0 until height) {
            for (j in 0 until width) {
                color = Color(newRed[i][j], newGreen[i][j], newBlue[i][j])
                bufferedImage.setRGB(j, i, color.rgb)
            }
        }

        ImageIO.write(bufferedImage, "bmp", newImage)
    }
}