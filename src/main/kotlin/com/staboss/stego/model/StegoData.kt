package com.staboss.stego.model

import com.staboss.stego.util.requireOrExit
import java.io.File

/**
 * [StegoData] contains all the necessary fields for the operation of the stego-methods
 *
 * @property sourceImage stego image
 * @property secretImage stego image with message
 * @property secretKey some file which contains special data for the stego-method
 * @property message secret message
 */
data class StegoData(
    var sourceImage: File,
    var secretImage: File = File("secret_image.bmp"),
    var secretKey: File = File("secret_key.txt"),
    var message: String? = null
) {

    inline fun secretImage(init: StegoData.() -> String) {
        val file = File(init.invoke(this))
        secretImage = file
    }

    inline fun secretKey(init: StegoData.() -> String) {
        val file = File(init.invoke(this))
        secretKey = file
    }

    inline fun message(init: StegoData.() -> String) {
        val messagePath = init.invoke(this)
        val textFile = File(messagePath)

        requireOrExit(textFile.exists()) {
            "Secret Text file does not exist : ${sourceImage.absolutePath}"
        }

        message = textFile.readText()
    }

    override fun toString(): String = buildString {
        appendln("Source Image Path   :  ${sourceImage.absolutePath}")
        appendln("Secret Image Path   :  ${secretImage.absolutePath}")
        appendln("Secret Key Path     :  ${secretKey.absolutePath}")
    }
}

inline fun createStegoData(sourceImage: String, init: StegoData.() -> Unit): StegoData {
    val imageFile = File(sourceImage)
    requireOrExit(imageFile.exists()) {
        "Source Image does not exist : ${imageFile.absolutePath}"
    }
    return StegoData(imageFile).apply(init)
}

inline fun createStegoData(sourceImage: File, init: StegoData.() -> Unit): StegoData {
    requireOrExit(sourceImage.exists()) {
        "Source Image does not exist : ${sourceImage.absolutePath}"
    }
    return StegoData(sourceImage).apply(init)
}
