package edu.ucne.registrotecnicos.presentation.prioridades

import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity

data class PrioridadUiState(
    val prioridadId: Int = 0,
    val descripcion: String = "",
    val diasCompromiso: String = "",
    val errorMessage: String? = null,
    val prioridades: List<PrioridadEntity> = emptyList(),
    val isLoading: Boolean = false,
)