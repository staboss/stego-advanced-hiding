package com.staboss.stego.pattern.factory

import com.staboss.stego.algorithm.StegoCOX
import com.staboss.stego.algorithm.StegoKDB
import com.staboss.stego.algorithm.StegoMethod
import com.staboss.stego.algorithm.StegoMethod.Algorithm
import com.staboss.stego.model.StegoData
import java.io.File

object StegoFactory {

    fun createMethod(algorithm: Algorithm, stegoData: StegoData): StegoMethod = with(stegoData) {
        when (algorithm) {
            Algorithm.KUTTER -> StegoKDB(sourceImage, secretImage, secretKey, message)
            Algorithm.COX -> StegoCOX(sourceImage, secretImage, secretKey, message)
        }
    }

    fun createMethod(
        algorithm: Algorithm,
        sourceImageFile: File,
        secretImageFile: File,
        secretKeyFile: File?,
        secretMessage: String? = null
    ): StegoMethod =
        when (algorithm) {
            Algorithm.KUTTER -> StegoKDB(sourceImageFile, secretImageFile, secretKeyFile, secretMessage)
            Algorithm.COX -> StegoCOX(sourceImageFile, secretImageFile, secretKeyFile, secretMessage)
        }
}
