package com.michaelflisar.translations.utils

import com.google.gson.Gson
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption


object FileUtil {

    fun listFolders(folder: File, filter: ((file: File) -> Boolean)?): List<File> {
        val subFolders = ArrayList<File>()
        val folders = folder.listFiles { f -> f.isDirectory }?.toList() ?: emptyList()
        folders.forEach {
            if (filter == null || filter.invoke(it)) {
                subFolders.add(it)
            }
            subFolders.addAll(listFolders(it, filter))
        }
        return subFolders
    }

    fun copyIfNotExists(fileFrom: File, fileTo: File): Boolean {
        return if (!fileTo.exists()) {
            Files.copy(fileFrom.toPath(), fileTo.toPath(), StandardCopyOption.REPLACE_EXISTING)
            true
        } else false
    }

    inline fun <reified T> readFile(file :File) : T? {
        return if (file.exists()) {
            val bufferedReader = file.bufferedReader()
            val settingsString = bufferedReader.use { it.readText() }
            Gson().fromJson(settingsString, T::class.java)
        } else {
            null
        }
    }
}