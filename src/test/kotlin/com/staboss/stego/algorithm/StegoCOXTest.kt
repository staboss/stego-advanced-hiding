package com.staboss.stego.algorithm

import com.staboss.stego.util.*
import org.junit.Assert.*
import org.junit.Test
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

class StegoCOXTest {

    private val message = "Copyright is a type of intellectual property that gives " +
            "its owner the exclusive right to make copies of a creative work, usually for a limited time."

    @Test
    fun embedTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File(RESULT_IMAGE_PATH)
        val alphaFile = File(ALPHA_FILE_PATH)

        val method = StegoCOX(sourceImageFile, secretImageFile, alphaFile, message)
        val result = method.embed()

        assertEquals(true, result)
    }

    @Test
    fun extractTest() {
        val sourceImageFile = File(RESULT_IMAGE_PATH)
        val secretImageFile = File(SOURCE_IMAGE_PATH)

        val method = StegoCOX(sourceImageFile, secretKey = secretImageFile)
        val result = method.extract()

        val difference = getDiff(message, result ?: error("RESULT IS NULL"))

        assertEquals(0.0, difference, 5.0)
    }

    @Test
    fun createDataForPlotTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File(RESULT_IMAGE_PATH)
        val alphaFile = File(ALPHA_FILE_PATH)

        val method = StegoCOX(sourceImageFile, secretImageFile, alphaFile, message)

        var psnr: String
        var rmse: String

        var original: BufferedImage
        var modified: BufferedImage

        val result = buildString {
            appendln("chars,psnr,rmse")
            repeat(1000) {
                println(it + 1)

                method.message = "s".repeat(it + 1)
                method.embed()

                original = ImageIO.read(File(SOURCE_IMAGE_PATH))
                modified = ImageIO.read(File(RESULT_IMAGE_PATH))

                psnr = StegoMath.psnr(original, modified)
                rmse = StegoMath.rmse(original, modified)

                appendln("${it + 1},$psnr,$rmse")
            }
        }

        File(DATA_FILE_PATH_COX).writeText(result)
    }
}