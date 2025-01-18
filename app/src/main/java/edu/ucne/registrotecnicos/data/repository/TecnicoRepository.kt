package edu.ucne.registrotecnicos.data.repository

import edu.ucne.registrotecnicos.data.local.database.RegistroDb
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import kotlinx.coroutines.flow.Flow

class TecnicoRepository(
    private val registroDb: RegistroDb
) {
    suspend fun save(tecnico: TecnicoEntity) {
        registroDb.tecnicoDao().save(tecnico)
    }

    suspend fun find(id: Int): TecnicoEntity? {
        return registroDb.tecnicoDao().find(id)
    }

    fun getAll(): Flow<List<TecnicoEntity>> {
        return registroDb.tecnicoDao().getAll()
    }

    suspend fun delete(tecnico: TecnicoEntity) {
        registroDb.tecnicoDao().delete(tecnico)
    }
}