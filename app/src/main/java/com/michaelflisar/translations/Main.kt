package com.michaelflisar.translations

import com.michaelflisar.translations.classes.Action
import com.michaelflisar.translations.classes.ProjectSettings
import com.michaelflisar.translations.classes.ResFile
import com.michaelflisar.translations.classes.Setup
import com.michaelflisar.translations.utils.FileUtil
import com.michaelflisar.translations.utils.L
import com.michaelflisar.translations.utils.Util
import org.omg.SendingContext.RunTime
import java.io.File

object Main {

    // ----------------------
    // action setup
    // ----------------------

    private val action: Action = Action.Import
    private val outputPath = "M:\\dev\\11 - libs (mine)\\Translations\\resources"
    private val settingsFileName = "settings.txt"
    private val infoFileName = "infos.md"
    private val defaultLanguage = "en"
    private val supportedLanguages = listOf(
        "es", "hu", "it", "ja", "pt-rBR", "sr", "tr", "zh-rCN"
    )

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
                processProject(index, project)
            }
            L.marker()
        } catch (e: Exception) {
            L.e(e.toString())
            L.e(e)
        }
    }

    private fun processProject(
        index: Int,
        project: Setup.Project
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
        val rootFolder = File(outputPath)
        val projectTargetFolder = File(rootFolder, project.name)
        projectTargetFolder.mkdirs()

        // 2) import files from project
        //    - find all res/values folders in the project
        //    - import the files from there if not already done
        //    - or update the imported files
        //    - create the settings files to remember source <=> target mappings
        val settings = ProjectSettings.read(projectTargetFolder, settingsFileName)
        val resourceFiles = FileUtil.listAllFiles(projectSourceFolder)
            .map { Util.isValidStringResourceFile(it, supportedLanguages) }
            .filter { it.valid }
            .map { ResFile(settings, it.defaultFile!!, it.file, it.language, projectTargetFolder) }
        resourceFiles
            .forEach { it.import() }
        settings.save(projectTargetFolder, settingsFileName)

        // 3) create info .md file for this project
        File(projectTargetFolder, infoFileName).writeText(generateInfoText(project))

        L.d("  - resourceFiles: ${resourceFiles.size}")
    }

    private fun export(project: Setup.Project) {
        L.d("  - exporting...")
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

    fun generateInfoText(project: Setup.Project): String {
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
            appendLine("That's it so far.")
        }
        return sb.toString()
    }
}