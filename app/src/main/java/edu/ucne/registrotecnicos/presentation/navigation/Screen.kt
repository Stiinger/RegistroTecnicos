package edu.ucne.registrotecnicos.presentation.navigation

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object TecnicoList : Screen() // Consulta
    @Serializable
    data class Tecnico(val tecnicoId: Int) : Screen() // Registro
}