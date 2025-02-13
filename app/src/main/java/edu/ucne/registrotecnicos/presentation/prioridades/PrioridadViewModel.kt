package edu.ucne.registrotecnicos.presentation.prioridades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.remote.Resource
import edu.ucne.registrotecnicos.data.remote.dto.PrioridadDto
import edu.ucne.registrotecnicos.data.repository.PrioridadRepository
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoUiState
import edu.ucne.registrotecnicos.presentation.tecnicos.toEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PrioridadViewModel @Inject constructor(
    private val prioridadRepository: PrioridadRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PrioridadUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getPrioridades()
    }

    fun save() {
        viewModelScope.launch {
            if (isValid()) {
                prioridadRepository.save(_uiState.value.toEntity())
            }
        }
    }

    fun delete(id: Int) {
        viewModelScope.launch {
            prioridadRepository.delete(id)
        }
    }

    fun update() {
        viewModelScope.launch {
            prioridadRepository.update(
                _uiState.value.prioridadId, PrioridadDto(
                    prioridadId = _uiState.value.prioridadId,
                    descripcion = _uiState.value.descripcion,
                    diasCompromiso = _uiState.value.diasCompromiso.toInt()
                )
            )
        }
    }

    fun new() {
        _uiState.value = PrioridadUiState()
    }

    fun onDescripcionChange(descripcion: String) {
        _uiState.update {
            it.copy(
                descripcion = descripcion,
                errorMessage = if (descripcion.isBlank()) "Debes rellenar el campo Descripción"
                else null
            )
        }
    }

    fun onDiasCompromisoChange(dias: String) {
        _uiState.update {
            val diasInt = dias.toIntOrNull()
            it.copy(
                diasCompromiso = dias,
                errorMessage = when {
                    diasInt == null -> "Valor no numérico"
                    diasInt <= 0 -> "Debe ingresar un valor mayor a 0"
                    else -> null
                }
            )
        }
    }

    fun find(prioridadId: Int) {
        viewModelScope.launch {
            if (prioridadId > 0) {
                val prioridadDto = prioridadRepository.find(prioridadId)
                if (prioridadDto.prioridadId != 0) {
                    _uiState.update {
                        it.copy(
                            prioridadId = prioridadDto.prioridadId,
                            descripcion = prioridadDto.descripcion,
                            diasCompromiso = prioridadDto.diasCompromiso.toString()
                        )
                    }
                }
            }
        }
    }

    fun getPrioridades() {
        viewModelScope.launch {
            prioridadRepository.getPrioridades().collectLatest { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                prioridades = result.data ?: emptyList(),
                                isLoading = false
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                errorMessage = result.message ?: "Error desconocido",
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }

    fun isValid(): Boolean {
        return uiState.value.descripcion.isNotBlank() && uiState.value.diasCompromiso.isNotBlank()
    }

}

fun PrioridadUiState.toEntity() = PrioridadDto(
    prioridadId = prioridadId,
    descripcion = descripcion,
    diasCompromiso = diasCompromiso.toInt()
)