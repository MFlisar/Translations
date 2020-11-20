package com.michaelflisar.translations.classes

sealed class Action {
    object Import : Action()
    object Export : Action()
    class CreateNewLanguage(val language: String) : Action()
    object CleanLanguages : Action()
}