package com.staboss.stego

import com.staboss.parser.Parser
import com.staboss.stego.algorithm.StegoMethod
import com.staboss.stego.model.createStegoData
import com.staboss.stego.pattern.command.ExtractCommand
import com.staboss.stego.pattern.command.StegoCommand
import com.staboss.stego.pattern.factory.StegoFactory
import com.staboss.stego.util.measureTime
import com.staboss.stego.util.requireOrExit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    if (args.contains("-h") || args.isEmpty()) {
        Parser.usage()
        return
    }

    val parser = Parser.getInstance()
    if (!parser.parseArgs(args)) return

    val mode = when {
        parser.embed -> StegoCommand.Type.EMBED
        else -> StegoCommand.Type.EXTRACT
    }

    val algorithm = getAlgorithm(parser.algorithm)

    requireOrExit(algorithm != null) {
        "There is no such type of method : ${parser.algorithm}"
    }

    val stegoData = createStegoData(parser.sourceImagePath) {
        with(parser) {
            resultImagePath?.let { secretImage { it } }
            secretKeyPath?.let { secretKey { it } }
            messageFile?.let { message { it } }
        }
    }

    val method = StegoFactory.createMethod(algorithm!!, stegoData)
    val command = StegoCommand.getCommand(mode)

    var time: Long = 0
    var result = ""

    runBlocking {
        val job = launch {
            println("Executing algorithm ...\n")
            launch(Dispatchers.IO) {
                time = measureTime {
                    result = command.execute(method)
                }
            }
        }
        job.join()
    }

    if (command is ExtractCommand) {
        println("Secret message: $result\n")
    } else {
        println("$result\n")
    }

    println("ALGORITHM COMPLETED in ${if (time < 1000) "$time ms" else "${time / 1000} s"}")
}

private fun getAlgorithm(algorithm: String): StegoMethod.Algorithm? = when (algorithm.toUpperCase()) {
    "KUTTER", "KDB" -> StegoMethod.Algorithm.KUTTER
    "COX" -> StegoMethod.Algorithm.COX
    "LSB" -> StegoMethod.Algorithm.LSB
    else -> null
}
