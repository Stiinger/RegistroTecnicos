package edu.ucne.registrotecnicos.data.remote

import edu.ucne.registrotecnicos.data.remote.dto.PrioridadDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.PUT

interface TicketManagerApi {
    @GET("api/Prioridades")
    suspend fun getPrioridades(): List<PrioridadDto>

    @GET("api/Prioridades/{id}")
    suspend fun getPrioridad(@Path("id") id: Int): PrioridadDto

    @POST("api/Prioridades")
    suspend fun savePrioridad(@Body prioridadDto: PrioridadDto?): PrioridadDto

    @PUT("api/Prioridades/{id}")
    suspend fun updatePrioridad(
        @Path("id") clienteId: Int,
        @Body prioridad: PrioridadDto
    ): Response<PrioridadDto>

    @DELETE("api/Prioridades/{id}")
    suspend fun deletePrioridad(@Path("id") id: Int): Response<Unit>
}