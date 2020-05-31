package com.staboss.stego.algorithm

import com.staboss.stego.util.requireOrExit
import com.staboss.stego.util.toBinary
import com.staboss.stego.util.toText
import java.io.File
import com.staboss.stego.util.StegoMath as stegoMath

class StegoLSB(
    sourceImage: File,
    secretImage: File? = null,
    secretKey: File? = null,
    message: String? = null
) : StegoMethod(sourceImage, secretImage, secretKey, message) {

    private val padding = "1000000000000"

    override fun embed(): Boolean {
        if (message == null) {
            println("Secret message is empty")
            return false
        }

        val binaryString = message!!.toBinary() + padding

        requireOrExit(binaryString.length < capacity) {
            "The message is too long to be embedded in this Image!"
        }

        var indexMSG = 0

        loop@
        for (i in 0 until height) {
            for (j in 0 until width) {
                if (indexMSG !in binaryString.indices) {
                    break@loop
                }
                if (indexMSG in binaryString.indices) {
                    red[i][j] = stegoMath.changeLSB(red[i][j], binaryString[indexMSG++])
                }
                if (indexMSG in binaryString.indices) {
                    green[i][j] = stegoMath.changeLSB(green[i][j], binaryString[indexMSG++])
                }
                if (indexMSG in binaryString.indices) {
                    blue[i][j] = stegoMath.changeLSB(blue[i][j], binaryString[indexMSG++])
                }
            }
        }

        writeImage(secretImage ?: File("secret_image.bmp"))
        return true
    }

    override fun extract(): String? {
        var messageBits = ""

        loop@
        for (i in 0 until height) {
            for (j in 0 until width) {
                messageBits += red[i][j].toBinary().last()
                if (messageBits.endsWith(padding)) {
                    break@loop
                }
                messageBits += green[i][j].toBinary().last()
                if (messageBits.endsWith(padding)) {
                    break@loop
                }
                messageBits += blue[i][j].toBinary().last()
                if (messageBits.endsWith(padding)) {
                    break@loop
                }
            }
        }

        return messageBits.take(messageBits.length - padding.length).toText()
    }
}