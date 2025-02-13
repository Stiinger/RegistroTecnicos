package edu.ucne.registrotecnicos.data.remote

import edu.ucne.registrotecnicos.data.remote.dto.PrioridadDto
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val ticketManagerApi: TicketManagerApi
) {
    suspend fun getPrioridades() = ticketManagerApi.getPrioridades()
    suspend fun getPrioridad(id: Int) = ticketManagerApi.getPrioridad(id)
    suspend fun savePrioridad(prioridadDto: PrioridadDto) = ticketManagerApi.savePrioridad(prioridadDto)
    suspend fun updatePrioridad(id: Int, prioridadDto: PrioridadDto) = ticketManagerApi.updatePrioridad(id, prioridadDto)
    suspend fun deletePrioridad(id: Int) = ticketManagerApi.deletePrioridad(id)
}