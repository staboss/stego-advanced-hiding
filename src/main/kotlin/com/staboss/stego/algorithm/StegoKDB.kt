package com.staboss.stego.algorithm

import com.staboss.coding.coder.NonSystematicCoder
import com.staboss.coding.decoder.Decoder
import com.staboss.stego.util.*
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.kotlin.toObservable
import java.io.File
import kotlin.random.Random as random

class StegoKDB(
    sourceImage: File,
    secretImage: File? = null,
    secretKey: File? = null,
    message: String? = null
) : StegoMethod(sourceImage, secretImage, secretKey, message) {

    private val sigma = 1
    private val lamda = 0.1

    private val initialState = "0000"
    private val polynomialFirst = "11001"
    private val polynomialSecond = "10111"

    private val coder by lazy {
        val coder = NonSystematicCoder(polynomialFirst, polynomialSecond)
        coder
    }

    private val brightness by lazy {
        calculateBrightness()
    }

    override fun embed(): Boolean {
        if (message == null) {
            println("Secret message is empty")
            return false
        }

        val binaryString = message!!.toBinary()
        val encoded = coder.encode(binaryString)

        requireOrExit(encoded.length < capacity) {
            "Secret message is too large to be embedded in this image"
        }

        val newBlue = Array(height) { IntArray(width) }.apply {
            for (i in 0 until height) {
                for (j in 0 until width) {
                    this[i][j] = blue[i][j]
                }
            }
        }

        var p: Pair<Int, Int>
        val pixels = mutableListOf<Pair<Int, Int>>()

        try {
            encoded.toList().toObservable()
                .subscribeBy(
                    onComplete = { writeImage(secretImage ?: File("secret_image.bmp"), newBlue = newBlue) },
                    onNext = { bit ->
                        p = randomPixel(pixels)
                        newBlue[p.first][p.second] = changeBlueValue(
                            blueValue = blue[p.first][p.second],
                            brightnessValue = brightness[p.first][p.second],
                            bitValue = bit
                        )
                        pixels.add(p)
                    },
                    onError = { it.printStackTrace() }
                )
        } catch (e: Exception) {
            println(e.localizedMessage)
            return false
        }

        Serialization.serialize(pixels, secretKey ?: File("secret_key.txt"))
        return true
    }

    override fun extract(): String? {
        val pixels = (Serialization.deserialize(secretKey ?: error("Secret key file is not defined")) as? List<*>)
            ?.asListOfType<Pair<Int, Int>>() ?: return null

        return try {
            val bits = pixels
                .map { extractBit(it.first, it.second) }
                .joinToString("")

            val vector = Decoder.createVectorValues(bits)

            with(coder) {
                val decoder = Decoder(states, initialState, vector)
                decode(decoder.decodedSequenceString).toText()
            }
        } catch (e: Exception) {
            println("Secret key file or image-container could be damaged")
            null
        }
    }

    private fun extractBit(x: Int, y: Int): Int {
        var value = 0.0

        var b1: Int
        var b2: Int
        var b3: Int
        var b4: Int

        for (i in 1..sigma) {
            b1 = blue[x][y + i]
            b2 = blue[x][y - i]
            b3 = blue[x + i][y]
            b4 = blue[x + i][y]
            value += b1 + b2 + b3 + b4
        }

        value /= 4 * sigma

        var delta = blue[x][y] - value

        if (delta.toInt() == 0) {
            if (blue[x][y] == 0) {
                delta = -0.5
            }
            if (blue[x][y] == 255) {
                delta = 0.5
            }
        }

        return if (delta > 0) 1 else 0
    }

    private fun randomPixel(pixels: List<Pair<Int, Int>>): Pair<Int, Int> {
        var x: Int
        var y: Int
        var pixel: Pair<Int, Int>
        while (true) {
            x = random.nextInt(height - sigma * 2) + sigma
            y = random.nextInt(width - sigma * 2) + sigma
            pixel = x to y
            if (!pixels.contains(pixel)) {
                return pixel
            }
        }
    }

    private fun changeBlueValue(blueValue: Int, brightnessValue: Double, bitValue: Char): Int {
        val beta = lamda * brightnessValue
        val alpha = if (bitValue == '1') 1 else -1
        val newBlueValue = (blueValue + alpha * beta).toInt()

        return when {
            newBlueValue > 255 -> 255
            newBlueValue < 0 -> 0
            else -> newBlueValue
        }
    }

    private fun calculateBrightness(): Array<DoubleArray> = run {
        val brightness = Array(height) { DoubleArray(width) }
        for (i in 0 until height) {
            for (j in 0 until width) {
                brightness[i][j] = 0.29890 * red[i][j] + 0.58662 * green[i][j] + 0.11448 * blue[i][j]
            }
        }
        brightness
    }
}