package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import kotlinx.coroutines.flow.Flow

class TecnicoRepository(
    private val tecnicoDb: TecnicoDb
) {
    suspend fun save(tecnico: TecnicoEntity) {
        tecnicoDb.tecnicoDao().save(tecnico)
    }

    suspend fun find(id: Int): TecnicoEntity? {
        return tecnicoDb.tecnicoDao().find(id)
    }

    fun getAll(): Flow<List<TecnicoEntity>> {
        return tecnicoDb.tecnicoDao().getAll()
    }

    suspend fun delete(tecnico: TecnicoEntity) {
        tecnicoDb.tecnicoDao().delete(tecnico)
    }
}