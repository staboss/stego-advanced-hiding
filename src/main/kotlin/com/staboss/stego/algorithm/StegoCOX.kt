package com.staboss.stego.algorithm

import com.staboss.stego.model.StegoImage
import com.staboss.stego.util.StegoMath.dctBlockSize
import com.staboss.stego.util.requireOrExit
import com.staboss.stego.util.toBinary
import com.staboss.stego.util.toText
import java.io.File
import com.staboss.stego.util.StegoMath as stegoMath

class StegoCOX(
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
            "Secret message is too large to be embedded in this image"
        }

        val colorBuffer = Array(dctBlockSize) { IntArray(dctBlockSize) }

        var bitIndex = 0
        var index: Pair<Int, Int>

        var s: Int
        var directDCT: Array<IntArray>
        var inverseDCT: Array<IntArray>

        val alpha = readAlpha(secretKey)

        requireOrExit(alpha != null) {
            "Invalid alpha value in file : ${secretKey?.absolutePath ?: "'secret key file is not defined'"}"
        }

        alpha ?: return false

        loop@
        for (i in 0 until (height - 8) step 8) {
            for (j in 0 until (width - 8) step 8) {
                if (bitIndex !in binaryString.indices) {
                    break@loop
                }

                for (k in 0 until dctBlockSize) {
                    for (l in 0 until dctBlockSize) {
                        colorBuffer[k][l] = red[i + k][j + l]
                    }
                }

                directDCT = stegoMath.directDCT(colorBuffer)
                index = stegoMath.getIndexOfMax(directDCT)

                s = if (binaryString[bitIndex] == '0') 1 else -1
                directDCT[index.first][index.second] = (directDCT[index.first][index.second] + alpha * s).toInt()

                inverseDCT = stegoMath.inverseDCT(directDCT)
                for (k in 0 until dctBlockSize) {
                    for (l in 0 until dctBlockSize) {
                        red[i + k][j + l] = inverseDCT[k][l]
                    }
                }

                bitIndex++
            }
        }

        writeImage(secretImage ?: File("secret_image.bmp"))
        return true
    }

    override fun extract(): String? {

        // In the COX method SECRET KEY is the ORIGINAL IMAGE
        val stegoImage: StegoImage
        try {
            stegoImage = StegoImage(secretKey ?: throw Exception())
        } catch (e: Exception) {
            println("Secret key for the COX algorithm – original image")
            return null
        }

        var origRedDct = Array(dctBlockSize) { IntArray(dctBlockSize) }
        var procRedDct = Array(dctBlockSize) { IntArray(dctBlockSize) }

        var messageBits = ""
        var index: Pair<Int, Int>

        loop@
        for (i in 0 until (height - 8) step 8) {
            for (j in 0 until (width - 8) step 8) {
                for (k in 0 until dctBlockSize) {
                    for (l in 0 until dctBlockSize) {
                        origRedDct[k][l] = stegoImage.red[i + k][j + l]
                        procRedDct[k][l] = red[i + k][j + l]
                    }
                }

                origRedDct = stegoMath.directDCT(origRedDct)
                procRedDct = stegoMath.directDCT(procRedDct)

                index = stegoMath.getIndexOfMax(origRedDct)
                messageBits += when {
                    procRedDct[index.first][index.second] > origRedDct[index.first][index.second] -> '0'
                    else -> '1'
                }

                if (messageBits.endsWith(padding)) {
                    break@loop
                }
            }
        }

        return messageBits.take(messageBits.length - padding.length).toText()
    }

    private fun readAlpha(file: File?): Double? = file?.readText(Charsets.UTF_8)?.toDoubleOrNull()
}
