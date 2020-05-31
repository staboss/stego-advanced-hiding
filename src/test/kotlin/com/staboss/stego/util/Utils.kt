package com.staboss.stego.util

fun getDiff(embedded: String, extracted: String): Double {
    val binaryOriginal = embedded.toBinary(bits = 8)
    val binaryExtracted = extracted.toBinary(bits = 8)

    var counter = 0.0
    for (i in binaryOriginal.indices) {
        if (binaryOriginal[i] != binaryExtracted[i]) {
            counter++
        }
    }

    return (counter / binaryOriginal.length) * 100
}