package edu.ucne.registrotecnicos.presentation.tecnicos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TecnicoViewModel @Inject constructor(
    private val tecnicoRepository: TecnicoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TecnicoUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getTecnicos()
    }

    fun save() {
        viewModelScope.launch {
            if (_uiState.value.nombres.isNotBlank() || _uiState.value.sueldo.toDoubleOrNull() != null) {
                tecnicoRepository.save(_uiState.value.toEntity())
            }
        }
    }

    fun onNombresChange(nombres: String) {
        _uiState.update {
            it.copy(
                nombres = nombres,
                errorMessage = if (nombres.isBlank()) "Debes rellenar el campo Nombre"
                else null
            )
        }
    }

    fun onSueldoChange(sueldo: String) {
        _uiState.update {
            it.copy(
                sueldo = sueldo,
                errorMessage = if (sueldo.toDoubleOrNull() == null) "Sueldo no numérico"
                else null
            )
        }
    }

    fun new() {
        _uiState.value = TecnicoUiState()
    }

    fun delete() {
        viewModelScope.launch {
            tecnicoRepository.delete(uiState.value.toEntity())
        }
    }

    private fun getTecnicos() {
        viewModelScope.launch {
            tecnicoRepository.getAll().collect { tecnicos ->
                println("Técnicos cargados: $tecnicos")
                _uiState.update {
                    it.copy(tecnicos = tecnicos)
                }
            }
        }
    }

    fun find(tecnicoId: Int) {
        viewModelScope.launch {
            if (tecnicoId > 0) {
                val tecnico = tecnicoRepository.find(tecnicoId)
                if (tecnico != null) {
                    _uiState.update {
                        it.copy(
                            tecnicoId = tecnico.tecnicoId,
                            nombres = tecnico.nombres,
                            sueldo = tecnico.sueldo.toString()
                        )
                    }
                }
            }
        }
    }
}

fun TecnicoUiState.toEntity() = TecnicoEntity(
    tecnicoId = this.tecnicoId,
    nombres = this.nombres,
    sueldo = this.sueldo.toDouble(),
)