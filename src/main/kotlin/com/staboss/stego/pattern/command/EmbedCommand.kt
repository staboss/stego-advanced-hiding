package com.staboss.stego.pattern.command

import com.staboss.stego.algorithm.StegoMethod

class EmbedCommand : StegoCommand() {

    override fun execute(stegoMethod: StegoMethod): String = when {
        stegoMethod.embed() -> "Success!"
        else -> "Algorithm Failed"
    }
}
