package com.michaelflisar.translations.utils

import java.io.PrintWriter

import java.io.StringWriter




object L {

    fun d(info: String) {
        println(info)
    }

    fun e(info: String) {
        println(info)
    }

    fun newLine() {
        println()
    }

    fun marker() {
        println("--------------------")
    }

    fun e(e: Exception) {
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        e.printStackTrace(pw)
        println(pw.toString())
    }

}