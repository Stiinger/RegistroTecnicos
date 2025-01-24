package edu.ucne.registrotecnicos.presentation.tecnicos

import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity

data class TecnicoUiState(
    val tecnicoId: Int? = null,
    val nombres: String = "",
    val sueldo: String = "",
    val errorMessage: String? = null,
    val tecnicos: List<TecnicoEntity> = emptyList()
)