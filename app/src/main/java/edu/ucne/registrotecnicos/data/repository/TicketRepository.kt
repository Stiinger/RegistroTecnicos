package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TicketRepository @Inject constructor(
    private val ticketDao: TicketDao
) {
    suspend fun save(ticket: TicketEntity) = ticketDao.save(ticket)

    suspend fun find(id: Int) = ticketDao.find(id)

    fun getAll(): Flow<List<TicketEntity>> = ticketDao.getAll()

    suspend fun delete(ticket: TicketEntity) = ticketDao.delete(ticket)
}