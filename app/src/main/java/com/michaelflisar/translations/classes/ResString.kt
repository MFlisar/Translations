package com.michaelflisar.translations.classes

import java.io.BufferedWriter

class ResString(
    val key: String,
    val value: String,
    val comment: String = ""
) {
    fun append(out: BufferedWriter, intentionLevel: Int) {
        val prefix = "   ".repeat(intentionLevel * 2)
        if (comment.isNotEmpty()) {
            out.appendLine()
            out.appendLine("$prefix <!-- <string name=\"$key\">$comment</string> -->")
        }
        out.appendLine("$prefix<string name=\"$key\">$value</string>")
    }

    fun appendCSV(out: BufferedWriter) {
        out.appendLine("\"$key\";\"$comment\";\"$value\"")
    }
}