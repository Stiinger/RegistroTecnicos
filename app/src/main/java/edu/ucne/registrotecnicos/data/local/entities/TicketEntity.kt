package edu.ucne.registrotecnicos.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "Tickets")
data class TicketEntity(
    @PrimaryKey
    val ticketId: Int? = null,
    val fecha: Date? = null,
    val prioridadId: Int? = null,
    val cliente: String = "",
    val asunto: String = "",
    val descripcion: String = "",
    val tecnicoId: Int? = null,
)