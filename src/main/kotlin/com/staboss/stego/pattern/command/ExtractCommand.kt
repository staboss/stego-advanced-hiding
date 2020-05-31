package com.staboss.stego.pattern.command

import com.staboss.stego.algorithm.StegoMethod

class ExtractCommand : StegoCommand() {

    override fun execute(stegoMethod: StegoMethod): String {
        return stegoMethod.extract() ?: "Algorithm Failed"
    }
}
