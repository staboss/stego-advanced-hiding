package com.staboss.stego.algorithm

import com.staboss.stego.util.SECRET_IMAGE_PATH_LSB
import com.staboss.stego.util.SOURCE_IMAGE_PATH
import com.staboss.stego.util.BIG_MESSAGE
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

class StegoLSBTest {

    @Test
    fun embedTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File(SECRET_IMAGE_PATH_LSB)

        val method = StegoLSB(sourceImageFile, secretImageFile, message = BIG_MESSAGE)
        val result = method.embed()

        assertEquals(true, result)
    }

    @Test
    fun extractTest() {
        val sourceImageFile = File(SECRET_IMAGE_PATH_LSB)

        val method = StegoLSB(sourceImageFile)
        val result = method.extract()

        assertEquals(BIG_MESSAGE, result)
    }
}