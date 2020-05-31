package com.staboss.stego.algorithm

import com.staboss.stego.model.StegoImage
import java.io.File

abstract class StegoMethod(
    sourceImage: File,
    var secretImage: File?,
    var secretKey: File? = null,
    var message: String? = null
): StegoImage(sourceImage) {

    abstract fun embed(): Boolean

    abstract fun extract(): String?

    enum class Algorithm {
        KUTTER, COX
    }
}
