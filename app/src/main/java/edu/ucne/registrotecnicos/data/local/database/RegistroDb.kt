package edu.ucne.registrotecnicos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import edu.ucne.registrotecnicos.data.local.converters.DateConverter
import edu.ucne.registrotecnicos.data.local.dao.MensajeDao
import edu.ucne.registrotecnicos.data.local.dao.PrioridadDao
import edu.ucne.registrotecnicos.data.local.dao.TecnicoDao
import edu.ucne.registrotecnicos.data.local.dao.TicketDao
import edu.ucne.registrotecnicos.data.local.entities.MensajeEntity
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity

@Database(
    entities = [
        TecnicoEntity::class,
        TicketEntity::class,
        MensajeEntity::class,
        PrioridadEntity::class
    ],
    version = 6,
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class RegistroDb : RoomDatabase() {
    abstract fun tecnicoDao(): TecnicoDao
    abstract fun ticketDao(): TicketDao
    abstract fun mensajeDao(): MensajeDao
    abstract fun prioridadDao(): PrioridadDao
}