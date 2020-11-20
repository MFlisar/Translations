package com.michaelflisar.translations.classes

import com.google.gson.Gson
import com.michaelflisar.translations.utils.FileUtil
import java.io.File

class ProjectSettings(
    val mappings: ArrayList<Mapping> = ArrayList()
) {

    companion object {
        fun read(projectTargetFolder: File, settingsFileName: String): ProjectSettings {
            val file = File(projectTargetFolder, settingsFileName)
            return FileUtil.readFile<ProjectSettings>(file) ?: ProjectSettings()
        }
    }

    fun add(source: File, target: File) {
        mappings.add(Mapping(source.absolutePath, target.absolutePath))
    }

    fun containsTarget(target: String) = mappings.find { it.target == target } != null

    fun save(projectTargetFolder: File, settingsFileName: String) {
        val file = File(projectTargetFolder, settingsFileName)
        file.writeText(Gson().toJson(this))
    }

    class Mapping(
        val source: String,
        val target: String
    )
}