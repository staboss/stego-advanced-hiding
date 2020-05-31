package com.staboss.stego.pattern.command

import com.staboss.stego.algorithm.StegoMethod

abstract class StegoCommand {

    abstract fun execute(stegoMethod: StegoMethod): String

    enum class Type {
        EMBED, EXTRACT
    }

    companion object {
        fun getCommand(commandType: Type) = when (commandType) {
            Type.EMBED -> EmbedCommand()
            Type.EXTRACT -> ExtractCommand()
        }
    }
}
