package edu.ucne.registrotecnicos.presentation.responses

data class MensajeConDatos(
    val contenido: String,
    val fechaHora: String,
    val nombreTecnico: String
)