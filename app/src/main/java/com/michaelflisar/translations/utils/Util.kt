package com.michaelflisar.translations.utils

import java.io.File

object Util {

    fun isValidStringResourceFile(file: File, supportedLanguages: List<String>): ResourceFileInfo {
        val parent = file.parentFile
        // 1) parent folder must be a "values.*" folder
        if (parent == null || !parent.nameWithoutExtension.startsWith("values"))
            return ResourceFileInfo(file, false)
        // 2) parent must be of a valid langues
        if (!supportedLanguages.map { "values-$it" }.contains(parent.nameWithoutExtension))
            return ResourceFileInfo(file, false)
        // 3) file must be an xml file
        if (file.extension != "xml")
            return ResourceFileInfo(file, false)
        val lang = parent.nameWithoutExtension.replace("values-", "")
        val defaultFile = File("${parent.parent}\\values\\${file.name}")
        return ResourceFileInfo(file, true, defaultFile, lang)
    }

    class ResourceFileInfo(
        val file: File,
        val valid: Boolean,
        val defaultFile: File? = null,
        val language: String = ""
    )
}