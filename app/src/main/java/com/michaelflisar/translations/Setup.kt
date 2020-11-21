package com.michaelflisar.translations

import com.michaelflisar.translations.classes.Action

object Setup {

    // ----------
    // languages
    // ----------

    // Available language codes: https://stackoverflow.com/questions/7973023/what-is-the-list-of-supported-languages-locales-on-android
    val DEFAULT_LANGUAGE = "en" // the default language in folders without a language code in it
    val IGNORED_LANGUAGES = listOf("de") // manually translated languages

    val OUTPUT_PATH = "M:\\dev\\11 - libs (mine)\\Translations\\resources"
    val SETTINGS_FILE_NAME = "settings.txt"
    val INFO_FILE_NAME = "infos.md"

    val ACTION: Action = Action.Import

    // ----------
    // projects / libraries
    // ----------

    // projects
    val PROJ_EL = Project("EL", "M:\\dev\\01 - apps\\EverywhereLauncher")
    val PROJ_COSY = Project("CoSy", "M:\\dev\\01 - apps\\CoSy")

    // libraries
    val LIB_SWISSARMY = Project("SwissArmy", "M:\\dev\\11 - libs (mine)\\SwissArmy")

    // list of all projects
    val projects = listOf(PROJ_EL, PROJ_COSY, LIB_SWISSARMY)

    // ----------
    // classes
    // ----------

    class Project(
        val name: String,
        val path: String,
        val dependencies: List<Project> = emptyList()
    )
}