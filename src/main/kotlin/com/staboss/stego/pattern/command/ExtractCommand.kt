package com.staboss.stego.pattern.command

import com.staboss.stego.algorithm.StegoMethod
import kotlin.system.exitProcess

class ExtractCommand : StegoCommand() {

    override fun execute(stegoMethod: StegoMethod): String {
        return stegoMethod.extract() ?: exitProcess(-1)
    }
}
