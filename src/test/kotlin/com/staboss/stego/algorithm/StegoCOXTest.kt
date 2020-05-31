package com.staboss.stego.algorithm

import com.staboss.stego.util.*
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class StegoCOXTest {

    @Test
    fun embedTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File(SECRET_IMAGE_PATH_COX)
        val alphaFile = File(ALPHA_FILE_PATH)

        val method = StegoCOX(sourceImageFile, secretImageFile, alphaFile, BIG_MESSAGE)
        val result = method.embed()

        assertEquals(true, result)
    }

    @Test
    fun extractTest() {
        val sourceImageFile = File(SECRET_IMAGE_PATH_COX)
        val secretImageFile = File(SOURCE_IMAGE_PATH)

        val method = StegoCOX(sourceImageFile, secretKey = secretImageFile)
        val result = method.extract()

        val difference = getDiff(BIG_MESSAGE, result ?: error("RESULT IS NULL"))

        assertEquals(0.0, difference, 5.0)
    }
}