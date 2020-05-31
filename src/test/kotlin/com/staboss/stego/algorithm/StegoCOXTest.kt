package com.staboss.stego.algorithm

import com.staboss.stego.util.ALPHA_FILE_PATH
import com.staboss.stego.util.RESULT_IMAGE_PATH
import com.staboss.stego.util.SOURCE_IMAGE_PATH
import com.staboss.stego.util.getDiff
import org.junit.Assert.*
import org.junit.Test
import java.io.File

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
}