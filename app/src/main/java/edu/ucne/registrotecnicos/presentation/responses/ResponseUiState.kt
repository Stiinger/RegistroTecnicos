package edu.ucne.registrotecnicos.presentation.responses

data class ResponseUiState(
    val nombreTecnico: String = "",
    val mensajes: List<MensajeConDatos> = emptyList(),
    val ticketId: Int = 0
)