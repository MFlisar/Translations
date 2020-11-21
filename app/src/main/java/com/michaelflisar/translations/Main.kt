package com.michaelflisar.translations

import com.michaelflisar.translations.classes.Action
import com.michaelflisar.translations.classes.ProjectSettings
import com.michaelflisar.translations.classes.ResFile
import com.michaelflisar.translations.utils.FileUtil
import com.michaelflisar.translations.utils.L
import com.michaelflisar.translations.utils.Util
import java.io.File

object Main {

    // ----------------------
    // main
    // ----------------------

    @JvmStatic
    fun main(arg: Array<String>) {
        try {
            if (Setup.projects.map { it.name }.toSet().size != Setup.projects.size) {
                throw Exception("Duplicate project names found!")
            }
            L.newLine()
            L.marker()
            Setup.projects.forEachIndexed { index, project ->
                processProject(index, project, Setup.ACTION)
            }
            L.marker()
        } catch (e: Exception) {
            L.e(e.toString())
            L.e(e)
        }
    }

    private fun processProject(
        index: Int,
        project: Setup.Project,
        action: Action
    ) {
        L.d("- Project ${index + 1}: ${project.name}")
        when (action) {
            Action.Import -> import(project)
            is Action.Export -> export(project)
            is Action.CreateNewLanguage -> createNewLanguage(project, action.language)
            Action.CleanLanguages -> cleanLanguage(project)
        }
    }

    // ----------------------
    // action functions
    // ----------------------

    private fun import(project: Setup.Project) {
        L.d("  - importing...")

        val projectSourceFolder = File(project.path)

        // 1) create folders for resource copies
        val rootFolder = File(Setup.OUTPUT_PATH)
        val projectTargetFolder = File(rootFolder, project.name)
        projectTargetFolder.mkdirs()

        // 2) import files from project
        //    - find all res/values folders in the project
        //    - import the files from there if not already done
        //    - or update the imported files
        //    - create the settings files to remember source <=> target mappings
        val settings = ProjectSettings.read(projectTargetFolder, Setup.SETTINGS_FILE_NAME)
        val resourceFiles = FileUtil.listAllFiles(projectSourceFolder)
            .map { Util.isValidStringResourceFile(it, Setup.DEFAULT_LANGUAGE, null) }
            .filter { it.valid && !it.isDefaultLanguage }
            .map { ResFile(settings, it.defaultFile!!, it.file, it.language, projectTargetFolder) }
            .filter { it.valid }
        resourceFiles
            .forEach { it.import() }
        settings.save(projectTargetFolder, Setup.SETTINGS_FILE_NAME)

        val counts = resourceFiles.map { it.default }.toSet()
            .map { t -> resourceFiles.find { it.default == t }!!.count }

        // 3) create info .md file for this project
        File(projectTargetFolder, Setup.INFO_FILE_NAME).writeText(generateInfoText(project, counts))

        L.d("  - resourceFiles: ${resourceFiles.size}")
    }

    private fun export(project: Setup.Project) {
        L.d("  - exporting...")

        // TODO: check if source and target have the some amount of %s and similar tags
    }

    private fun createNewLanguage(
        project: Setup.Project,
        language: String
    ) {
        L.d("  - creating language [$language]...")
    }

    private fun cleanLanguage(project: Setup.Project) {
        L.d("  - cleaning language...")
    }

    fun generateInfoText(project: Setup.Project, counts: List<Int>): String {
        val sb = StringBuilder().apply {
            appendLine("# Infos for project ${project.name}")
            appendLine("I appreciate any help with translating my app. Of course, any person that helps translating my app will get the app for free in return.")
            appendLine()
            appendLine("# Contribution process")
            appendLine("- Inform me via mail that you want to help to translate this app and add following informations")
            appendLine("  - app name")
            appendLine("  - language")
            appendLine("- Wait for my answer mail - I will prepare your language if it does not yet exist")
            appendLine("- Clone this project and make your changes")
            appendLine("- Create a pull request to submit your changes into this repository")
            appendLine()
            appendLine("That's it so far.")
            appendLine()
            appendLine("# Infos")
            appendLine("- Strings: ${counts.joinToString()}")
        }
        return sb.toString()
    }
}