package com.michaelflisar.translations.classes

import com.michaelflisar.translations.utils.FileUtil
import com.michaelflisar.translations.utils.L
import java.io.File

class ResFile(
    settings: ProjectSettings,
    val default: File,
    val source: File,
    val language: String,
    val projectRootsFolder: File
) {

    val target: File
    var count: Int = 0
        private set

    init {
        val existingMapping = settings.mappings.find { it.source == source.absolutePath }
        val existingTarget = existingMapping?.target?.let { File(it) }

        if (existingTarget != null) {
            target = existingTarget
        } else {
            target = createNewFile(settings)
            settings.add(source, target)
        }
    }

    /*
     * this is not optimised, it's just the most simple solution to make it easy
     */
    fun import() {

        // read original and translated resources
        val originalStrings = FileUtil.readStringResourceFile(source)
        val translatedString = FileUtil.readStringResourceFile(target)
        val defaultString = FileUtil.readStringResourceFile(default)

        count = defaultString.size

        // update files if already existed
        val finalTranslatedString = ArrayList<ResString>()
        for (default in defaultString) {
            val original = originalStrings.find { it.key == default.key }
            val translated = translatedString.find { it.key == default.key }
            val finalValue = translated?.value ?: original?.value ?: ""
            finalTranslatedString.add(ResString(default.key, finalValue, default.value))
        }
        FileUtil.writeStringResourceFile(target, finalTranslatedString)
        //val target2 = File(target.absolutePath.replace(".xml", ".csv"))
        //FileUtil.writeStringResourceFileCSV(target2, finalTranslatedString)

        L.d("$source => $target")
    }

    private fun createNewFile(settings: ProjectSettings): File {
        val name = source.nameWithoutExtension
        val ext = source.extension
        var index = 0

        val getFile = {
            File(projectRootsFolder, "${language}_${name}_$index.$ext")
        }

        var file = getFile()
        while (settings.containsTarget(file.absolutePath)) {
            index++
            file = getFile()
        }

        return file
    }
}