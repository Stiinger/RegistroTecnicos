package edu.ucne.registrotecnicos.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.ucne.registrotecnicos.data.local.database.RegistroDb
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context) =
        Room.databaseBuilder(
            appContext,
            RegistroDb::class.java,
            "Registro.db"
        ).fallbackToDestructiveMigration()
            .build()

    @Provides
    fun provideTicketDao(registroDb: RegistroDb) = registroDb.ticketDao()

    @Provides
    fun provideTecnicoDao(registroDb: RegistroDb) = registroDb.tecnicoDao()
}