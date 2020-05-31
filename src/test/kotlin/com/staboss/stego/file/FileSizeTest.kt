package com.staboss.stego.file

import com.staboss.stego.algorithm.StegoCOX
import com.staboss.stego.algorithm.StegoKDB
import com.staboss.stego.util.*
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import kotlin.math.abs

class FileSizeTest {

    @Test
    fun fileSizeKutterTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File(SECRET_IMAGE_PATH_KDB)
        val secretKeyFile = File(KEY_FILE_PATH)

        StegoKDB(
            sourceImage = sourceImageFile,
            secretImage = secretImageFile,
            secretKey = secretKeyFile,
            message = BIG_MESSAGE
        ).embed()

        val originalLength = File(SOURCE_IMAGE_PATH).length()
        val modifiedLength = File(SECRET_IMAGE_PATH_KDB).length()

        val difference = abs(modifiedLength - originalLength)

        assertEquals(84, difference)
    }

    @Test
    fun fileSizeCoxTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File(SECRET_IMAGE_PATH_COX)
        val secretKeyFile = File(ALPHA_FILE_PATH)

        StegoCOX(
            sourceImage = sourceImageFile,
            secretImage = secretImageFile,
            secretKey = secretKeyFile,
            message = BIG_MESSAGE
        ).embed()

        val originalLength = File(SOURCE_IMAGE_PATH).length()
        val modifiedLength = File(SECRET_IMAGE_PATH_COX).length()

        val difference = abs(modifiedLength - originalLength)

        assertEquals(84, difference)
    }
}