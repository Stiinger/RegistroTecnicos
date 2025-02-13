package edu.ucne.registrotecnicos.data.repository

import android.util.Log
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.PrioridadDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class PrioridadRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
) {
    fun getPrioridades(): Flow<Resource<List<PrioridadDto>>> = flow {
        try {
            emit(Resource.Loading())
            val clientes = remoteDataSource.getPrioridades()
            emit(Resource.Success(clientes))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            Log.e("PrioridadRepository", "HttpException: $errorMessage")
            emit(Resource.Error("Error de conexion $errorMessage"))
        } catch (e: Exception) {

            Log.e("PrioridadRepository", "Exception: ${e.message}")
            emit(Resource.Error("Error: ${e.message}"))
        }
    }

    suspend fun update(id: Int, prioridadDto: PrioridadDto) =
        remoteDataSource.updatePrioridad(id, prioridadDto)

    suspend fun find(id: Int) = remoteDataSource.getPrioridad(id)
    suspend fun save(prioridadDto: PrioridadDto) = remoteDataSource.savePrioridad(prioridadDto)
    suspend fun delete(id: Int) = remoteDataSource.deletePrioridad(id)
}