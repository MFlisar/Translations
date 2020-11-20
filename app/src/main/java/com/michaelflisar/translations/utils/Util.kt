package com.michaelflisar.translations.utils

import java.io.File

object Util {

    fun isValidStringResourceFile(file: File, supportedLanguages: List<String>? = null): ResourceFileInfo {
        val parent = file.parentFile
        // 1) parent folder must be a "values.*" folder
        if (parent == null || !parent.nameWithoutExtension.startsWith("values"))
            return ResourceFileInfo(file, false)
        // 2) parent must be of a valid langues
        if (supportedLanguages != null && !supportedLanguages.map { "values-$it" }.contains(parent.nameWithoutExtension))
            return ResourceFileInfo(file, false)
        // 3) file must be an xml file
        if (file.extension != "xml")
            return ResourceFileInfo(file, false)
        // 4) file must contain string in name
        if (!file.name.contains("string", true))
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