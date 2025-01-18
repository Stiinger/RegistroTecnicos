package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.database.RegistroDb
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import kotlinx.coroutines.flow.Flow

class TicketRepository(
    private val registroDb: RegistroDb
) {
    suspend fun save(ticket: TicketEntity) {
        registroDb.ticketDao().save(ticket)
    }

    suspend fun find(id: Int): TicketEntity? {
        return registroDb.ticketDao().find(id)
    }

    fun getAll(): Flow<List<TicketEntity>> {
        return registroDb.ticketDao().getAll()
    }

    suspend fun delete(ticket: TicketEntity) {
        registroDb.ticketDao().delete(ticket)
    }
}