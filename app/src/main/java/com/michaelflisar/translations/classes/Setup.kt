package com.michaelflisar.translations.classes

object Setup {

    // projects
    val PROJ_EL = Project("EL", "M:\\dev\\01 - apps\\EverywhereLauncher")
    val PROJ_COSY = Project("CoSy", "M:\\dev\\01 - apps\\CoSy")

    // libraries
    val LIB_SWISSARMY = Project("SwissArmy", "M:\\dev\\11 - libs (mine)\\SwissArmy")

    // list of all projects
    val projects = listOf(PROJ_EL, PROJ_COSY, LIB_SWISSARMY)

    // dependencies
    val dependencies = listOf(
        Dependency(PROJ_EL, listOf(LIB_SWISSARMY)),
        Dependency(PROJ_COSY, listOf(LIB_SWISSARMY))
    )

    // ----------
    // classes
    // ----------

    class Project(
        val name: String,
        val path: String
    )

    class Dependency(
        val project: Project,
        val dependencies: List<Project>
    )

}