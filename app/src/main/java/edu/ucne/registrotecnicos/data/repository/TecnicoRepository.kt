package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TecnicoRepository @Inject constructor(
    private val tecnicoDao: TecnicoDao
) {
    suspend fun save(tecnico: TecnicoEntity) = tecnicoDao.save(tecnico)

    suspend fun find(id: Int) = tecnicoDao.find(id)

    fun getAll(): Flow<List<TecnicoEntity>> = tecnicoDao.getAll()

    suspend fun delete(tecnico: TecnicoEntity) = tecnicoDao.delete(tecnico)
}