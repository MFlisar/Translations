package com.michaelflisar.translations.classes

import java.io.BufferedWriter

class ResString(
    val key: String,
    val value: String,
    val comment: String = ""
) {
    fun append(out: BufferedWriter, intentionLevel: Int) {
        val prefix = "   ".repeat(intentionLevel * 2)
        val addon = comment.takeIf { it.length > 0 }?.let { " <!-- $it -->" } ?: ""
        //out.appendLine("$prefix<string name=\"$key\">$value</string>$addon")

        if (comment.isNotEmpty()) {
            out.appendLine()
            out.appendLine("$prefix <!-- <string name=\"$key\">$comment</string> -->")
//            out.appendLine("$prefix <!-- $comment -->")
        }
        out.appendLine("$prefix<string name=\"$key\">$value</string>")

    }
}