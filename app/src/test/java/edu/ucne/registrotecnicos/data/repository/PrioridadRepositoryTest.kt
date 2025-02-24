package edu.ucne.registrotecnicos.data.repository

import app.cash.turbine.test
import com.google.common.truth.Truth
import edu.ucne.registrotecnicos.data.local.dao.PrioridadDao
import edu.ucne.registrotecnicos.data.local.entities.PrioridadEntity
import edu.ucne.registrotecnicos.data.remote.RemoteDataSource
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.PrioridadDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import retrofit2.Response

class PrioridadRepositoryTest {

    @Test // Should add a prioridad
    fun addPrioridad() = runTest{
        // Given
        val prioridad = PrioridadDto(
            prioridadId = 1,
            descripcion = "Prioridad 1",
            diasCompromiso = 1
        )

        val prioridadRemoteDataSource = mockk<RemoteDataSource>()
        val prioridadDao = mockk<PrioridadDao>()
        val repository = PrioridadRepository(prioridadDao, prioridadRemoteDataSource)

        coEvery { prioridadRemoteDataSource.savePrioridad(any()) } returns prioridad

        // When
        repository.save(prioridad)

        // Then
        coVerify { prioridadRemoteDataSource.savePrioridad(prioridad) }
    }

    @Test // Should return a prioridad
    fun getPrioridad() = runTest{
        //Given
        val prioridad = PrioridadDto(
            prioridadId = 1,
            descripcion = "Prioridad 1",
            diasCompromiso = 1
        )

        val prioridadRemoteDataSource = mockk<RemoteDataSource>()
        val prioridadDao = mockk<PrioridadDao>()
        val repository = PrioridadRepository(prioridadDao, prioridadRemoteDataSource)

        coEvery { prioridadRemoteDataSource.getPrioridad(prioridad.prioridadId) } returns prioridad

        //When
        repository.find(prioridad.prioridadId)

        //Then
        coVerify { prioridadRemoteDataSource.getPrioridad(prioridad.prioridadId) }
    }

    @Test // Should delete a prioridad
    fun deletePrioridad() = runTest{
        //Given
        val prioridad = PrioridadDto(
            prioridadId = 1,
            descripcion = "Prioridad 1",
            diasCompromiso = 1
        )

        val prioridadRemoteDataSource = mockk<RemoteDataSource>()
        val prioridadDao = mockk<PrioridadDao>()
        val repository = PrioridadRepository(prioridadDao, prioridadRemoteDataSource)

        coEvery { prioridadRemoteDataSource.deletePrioridad(prioridad.prioridadId) } returns Response.success(Unit)

        //When
        repository.delete(prioridad.prioridadId)

        //Then
        coVerify { prioridadRemoteDataSource.deletePrioridad(prioridad.prioridadId) }
    }

    @Test // Should update an prioridad
    fun updatePrioridad() = runTest{
        //Given
        val prioridad = PrioridadDto(
            prioridadId = 1,
            descripcion = "Prioridad 1",
            diasCompromiso = 1
        )

        val prioridadRemoteDataSource = mockk<RemoteDataSource>()
        val prioridadDao = mockk<PrioridadDao>()
        val repository = PrioridadRepository(prioridadDao, prioridadRemoteDataSource)

        coEvery { prioridadRemoteDataSource.updatePrioridad(prioridad.prioridadId, prioridad) } returns Response.success(prioridad)

        //When
        repository.update(prioridad.prioridadId, prioridad)

        //Then
        coVerify { prioridadRemoteDataSource.updatePrioridad(prioridad.prioridadId, prioridad) }
    }

    @Test // Should return a flow of prioridades
    fun getPrioridades() = runTest {
        // Given
        val prioridades = listOf(
            PrioridadEntity(1, "Prioridad 1", 1),
        )

        val prioridadRemoteDataSource = mockk<RemoteDataSource>()
        val prioridadDao = mockk<PrioridadDao>()
        val repository = PrioridadRepository(prioridadDao, prioridadRemoteDataSource)

        coEvery { prioridadDao.getAll() } returns flowOf(prioridades)

        //When
        repository.getPrioridades().test {
            //Then
            Truth.assertThat(awaitItem() is Resource.Loading).isTrue()

            Truth.assertThat(awaitItem().data).isEqualTo(prioridades)

            cancelAndIgnoreRemainingEvents()
        }
    }
}