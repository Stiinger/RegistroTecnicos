package edu.ucne.registrotecnicos.data.repository

import android.util.Log
import edu.ucne.registrotecnicos.data.local.dao.PrioridadDao
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.PrioridadDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class PrioridadRepository @Inject constructor(
    private val prioridadDao: PrioridadDao,
    private val remoteDataSource: RemoteDataSource,
) {
    fun getPrioridades(): Flow<Resource<List<PrioridadEntity>>> = flow {
        emit(Resource.Loading())
        try {
            val prioridadRemoto = remoteDataSource.getPrioridades()

            val listaPrioridades = prioridadRemoto.map { dto ->
                PrioridadEntity(
                    prioridadId = dto.prioridadId,
                    descripcion = dto.descripcion,
                    diasCompromiso = dto.diasCompromiso
                )
            }
            prioridadDao.save(listaPrioridades)
            emit(Resource.Success(listaPrioridades))
        } catch (e: HttpException) {
            val errorMessage = e.response()?.errorBody()?.string() ?: e.message()
            emit(Resource.Error("Error de conexión $errorMessage"))
        } catch (e: Exception) {
            val localPrioridades = prioridadDao.getAll().first()

            if (localPrioridades.isNotEmpty())
                emit(Resource.Success(localPrioridades))
            else
                emit(Resource.Error("Error de conexión: ${e.message}"))
        }
    }

    suspend fun update(id: Int, prioridadDto: PrioridadDto) =
        remoteDataSource.updatePrioridad(id, prioridadDto)

    suspend fun find(id: Int) = remoteDataSource.getPrioridad(id)
    suspend fun save(prioridadDto: PrioridadDto) = remoteDataSource.savePrioridad(prioridadDto)
    suspend fun delete(id: Int) = remoteDataSource.deletePrioridad(id)
}