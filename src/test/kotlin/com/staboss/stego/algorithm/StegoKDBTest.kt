package com.staboss.stego.algorithm

import com.staboss.stego.util.*
import org.junit.Assert.*
import org.junit.Test
import java.io.File

class StegoKDBTest {

    @Test
    fun embedTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File(SECRET_IMAGE_PATH_KDB)
        val secretKeyFile = File(KEY_FILE_PATH)

        val method = StegoKDB(sourceImageFile, secretImageFile, secretKeyFile, MESSAGE)
        val result = method.embed()

        assertEquals(true, result)
    }

    @Test
    fun extractTest() {
        val sourceImageFile = File(SECRET_IMAGE_PATH_KDB)
        val secretKeyFile = File(KEY_FILE_PATH)

        val method = StegoKDB(sourceImageFile, secretKey = secretKeyFile)
        val result = method.extract()

        val difference = getDiff(MESSAGE, result ?: error("RESULT IS NULL"))
        // println(String.format("%.2f", difference).replace(",", ".") + " %")

        assertEquals(0.0, difference, DELTA)
    }

    @Test
    fun averageTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File(SECRET_IMAGE_PATH_KDB)
        val secretKeyFile = File(KEY_FILE_PATH)

        var methodEmbed: StegoMethod
        var methodExtract: StegoMethod

        val n = 10
        var total = 0.0
        var result: String?

        repeat(n) {
            methodEmbed = StegoKDB(sourceImageFile, secretImageFile, secretKeyFile, MESSAGE)
            methodEmbed.embed()

            methodExtract = StegoKDB(secretImageFile, secretKey = secretKeyFile)
            result = methodExtract.extract()

            total += getDiff(MESSAGE, result ?: error("RESULT IS NULL"))
        }

        val difference = total / n
        assertEquals(0.0, difference, 25.0)
    }
}