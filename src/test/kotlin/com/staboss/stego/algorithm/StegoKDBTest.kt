package com.staboss.stego.algorithm

import com.staboss.stego.util.*
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class StegoKDBTest {

    private val message = "Copyright is a type of intellectual property."

    @Test
    fun embedTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File(RESULT_IMAGE_PATH)
        val secretKeyFile = File(KEY_FILE_PATH)

        val method = StegoKDB(sourceImageFile, secretImageFile, secretKeyFile, message)
        val result = method.embed()

        assertEquals(true, result)
    }

    @Test
    fun extractTest() {
        val sourceImageFile = File(RESULT_IMAGE_PATH)
        val secretKeyFile = File(KEY_FILE_PATH)

        val method = StegoKDB(sourceImageFile, secretKey = secretKeyFile)
        val result = method.extract()

        println(result)

        val difference = getDiff(message, result ?: error("RESULT IS NULL"))
        assertEquals(0.0, difference, 15.0)
    }

    @Test
    fun averageTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File(RESULT_IMAGE_PATH)
        val secretKeyFile = File(KEY_FILE_PATH)

        var methodEmbed: StegoMethod
        var methodExtract: StegoMethod

        val n = 100
        var total = 0.0
        var result: String?

        repeat(n) {
            methodEmbed = StegoKDB(sourceImageFile, secretImageFile, secretKeyFile, message)
            methodEmbed.embed()

            methodExtract = StegoKDB(secretImageFile, secretKey = secretKeyFile)
            result = methodExtract.extract()

            total += getDiff(message, result ?: error("RESULT IS NULL"))
        }

        val difference = total / n
        assertEquals(0.0, difference, 15.0)
    }
}