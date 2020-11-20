package com.michaelflisar.translations.classes

import com.michaelflisar.translations.utils.FileUtil
import com.michaelflisar.translations.utils.L
import java.io.File

class ResFile(
    settings: ProjectSettings,
    val source: File,
    val projectRootsFolder: File
) {

    val target: File

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

    fun import() {
        // import file if it does not yet exist
        val copied = FileUtil.copyIfNotExists(source, target)
        // update files if already existed
        if (!copied) {
            // TODO
            // update
        }

        L.d("$source => $target (copied: $copied)")
    }

    private fun createNewFile(settings: ProjectSettings): File {
        val name = source.nameWithoutExtension
        val ext = source.extension
        var index = 0

        val getFile = {
            File(projectRootsFolder, "${name}_$index.$ext")
        }

        var file = getFile()
        while (settings.containsTarget(file.absolutePath)) {
            index++
            file = getFile()
        }

        return file
    }
}