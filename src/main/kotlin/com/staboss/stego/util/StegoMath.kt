package com.staboss.stego.util

import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.*

object StegoMath {

    const val dctBlockSize = 8

    fun directDCT(block: Array<IntArray>): Array<IntArray> {
        require(block.size == dctBlockSize && block[0].size == dctBlockSize) {
            "The block must be 8 x 8 pixels"
        }

        val dctCoefficients = Array(dctBlockSize) { IntArray(dctBlockSize) }

        var coefficient1: Double
        var coefficient2: Double

        for (k in 0 until dctBlockSize) {
            for (l in 0 until dctBlockSize) {
                coefficient1 = if (k == 0) sqrt(1.0 / dctBlockSize) else sqrt(2.0 / dctBlockSize)
                coefficient2 = if (l == 0) sqrt(1.0 / dctBlockSize) else sqrt(2.0 / dctBlockSize)

                var sum = 0.0
                for (i in 0 until dctBlockSize) {
                    for (j in 0 until dctBlockSize) {
                        sum += block[i][j] *
                                cos(((2 * i + 1) * PI) / (2 * dctBlockSize) * k) *
                                cos(((2 * j + 1) * PI) / (2 * dctBlockSize) * l)
                    }
                }

                dctCoefficients[k][l] = (coefficient1 * coefficient2 * sum).toInt()
            }
        }

        return dctCoefficients
    }

    fun inverseDCT(block: Array<IntArray>): Array<IntArray> {
        require(block.size == dctBlockSize && block[0].size == dctBlockSize) {
            "The block must be 8 x 8 pixels"
        }

        val dctCoefficients = Array(dctBlockSize) { IntArray(dctBlockSize) }

        var coefficient1: Double
        var coefficient2: Double

        for (i in 0 until dctBlockSize) {
            for (j in 0 until dctBlockSize) {

                var sum = 0.0
                for (k in 0 until dctBlockSize) {
                    for (l in 0 until dctBlockSize) {
                        coefficient1 = if (k == 0) sqrt(1.0 / dctBlockSize) else sqrt(2.0 / dctBlockSize)
                        coefficient2 = if (l == 0) sqrt(1.0 / dctBlockSize) else sqrt(2.0 / dctBlockSize)
                        sum += coefficient1 * coefficient2 * block[k][l] *
                                cos(((2 * i + 1) * PI) / (2 * dctBlockSize) * k) *
                                cos(((2 * j + 1) * PI) / (2 * dctBlockSize) * l)
                    }
                }

                dctCoefficients[i][j] = clip(sum)
            }
        }

        return dctCoefficients
    }

    private fun clip(x: Double): Int = when {
        x < 0 -> 0
        x > 255 -> 255
        else -> x.toInt()
    }

    fun getIndexOfMax(array: Array<IntArray>): Pair<Int, Int> {
        var x = 1
        var y = 0

        for (i in array.indices) {
            for (j in array.indices) {
                if (i == 0 && j == 0) {
                    continue
                }
                if (array[i][j] > array[y][x]) {
                    x = j
                    y = i
                }
            }
        }

        return y to x
    }

    /**
     * Computes the MSE value of two images
     *
     * @param original Original Image
     * @param modified Modified Image
     * @return MSE value
     */
    fun mse(original: BufferedImage, modified: BufferedImage): Double {
        assert(
            original.type == modified.type && original.height == modified.height && original.width == modified.width
        )

        val width = original.width
        val height = original.height

        var colorOriginal: Color
        var colorModified: Color

        var mse = 0.0
        for (y in 0 until height) {
            for (x in 0 until width) {
                colorOriginal = Color(original.getRGB(x, y))
                colorModified = Color(modified.getRGB(x, y))

                mse += (colorOriginal.red - colorModified.red).toDouble().pow(2.0)
                mse += (colorOriginal.green - colorModified.green).toDouble().pow(2.0)
                mse += (colorOriginal.blue - colorModified.blue).toDouble().pow(2.0)
            }
        }

        mse /= width * height * 3
        return mse
    }

    /**
     * Computes the PSNR value of two images
     *
     * @param original Original Image
     * @param modified Modified Image
     * @return PSNR value
     */
    fun psnr(original: BufferedImage, modified: BufferedImage): String {
        val mse = mse(original, modified)
        val psnr = 10.0 * log10(255.0.pow(2.0) / mse)
        return String.format("%.4f", psnr).replace(",", ".")
    }

    /**
     * Computes the RMSE value of two images
     *
     * @param original Original Image
     * @param modified Modified Image
     * @return RMSE value
     */
    fun rmse(original: BufferedImage, modified: BufferedImage): String {
        val mse = mse(original, modified)
        val rmse = sqrt(mse)
        return String.format("%.6f", rmse).replace(",", ".")
    }

    /**
     * Log base 10
     *
     * @param x double value
     * @return log10(x)
     */
    fun log10(x: Double): Double {
        return ln(x) / ln(10.0)
    }

    fun changeLSB(component: Int, bit: Char): Int {
        return changeLSB(component, if (bit == '1') 1 else 0)
    }

    fun changeLSB(component: Int, bit: Int): Int {
        return component and 1.inv() or bit
    }
}
