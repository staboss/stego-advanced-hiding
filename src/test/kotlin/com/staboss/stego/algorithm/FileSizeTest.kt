package com.staboss.stego.algorithm

import com.staboss.stego.util.ALPHA_FILE_PATH
import com.staboss.stego.util.KEY_FILE_PATH
import com.staboss.stego.util.RESULT_IMAGE_PATH
import com.staboss.stego.util.SOURCE_IMAGE_PATH
import org.junit.Assert.*
import org.junit.Test
import java.io.File
import kotlin.math.abs

class FileSizeTest {

    private val message =
        "But I must explain to you how all this mistaken idea of " +
                "denouncing pleasure and praising pain was born and " +
                "I will give you a complete account of the system, and " +
                "expound the actual teachings of the great explorer of " +
                "the truth, the master-builder of human happiness. No one " +
                "rejects, dislikes, or avoids pleasure itself, because it is " +
                "pleasure, but because those who do not know how to pursue pleasure " +
                "rationally encounter consequences that are extremely painful. Nor again " +
                "is there anyone who loves or pursues or desires to obtain pain of itself, " +
                "because it is pain, but because occasionally circumstances occur in which " +
                "toil and pain can procure him some great pleasure. To take a trivial example, " +
                "which of us ever undertakes laborious physical exercise, except to obtain some " +
                "advantage from it?"

    @Test
    fun fileSizeKutterTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File("src/test/resources/res_kdb.bmp")
        val secretKeyFile = File(KEY_FILE_PATH)

        StegoKDB(sourceImageFile, secretImageFile, secretKeyFile, message).embed()

        val originalLength = File(SOURCE_IMAGE_PATH).length()
        val modifiedLength = File("src/test/resources/res_kdb.bmp").length()

        val difference = abs(modifiedLength - originalLength)

        assertEquals(84, difference)
    }

    @Test
    fun fileSizeCoxTest() {
        val sourceImageFile = File(SOURCE_IMAGE_PATH)
        val secretImageFile = File("src/test/resources/res_cox.bmp")
        val secretKeyFile = File(ALPHA_FILE_PATH)

        StegoCOX(sourceImageFile, secretImageFile, secretKeyFile, message).embed()

        val originalLength = File(SOURCE_IMAGE_PATH).length()
        val modifiedLength = File("src/test/resources/res_cox.bmp").length()

        val difference = abs(modifiedLength - originalLength)

        assertEquals(84, difference)
    }
}