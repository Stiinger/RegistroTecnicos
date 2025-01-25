package edu.ucne.registrotecnicos.presentation.tickets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.data.repository.TicketRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TicketViewModel @Inject constructor(
    private val ticketRepository: TicketRepository,
    private val tecnicoRepository: TecnicoRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(TicketUiState())
    val uiState = _uiState.asStateFlow()

    private val _tecnicosList = MutableStateFlow<List<TecnicoEntity>>(emptyList())
    val tecnicosList = _tecnicosList.asStateFlow()

    init {
        getTickets()
        getTecnicos()
    }

    fun save() {
        viewModelScope.launch {
            if (isValid()) {
                ticketRepository.save(_uiState.value.toEntity())
            }
        }
    }

    fun onAsuntoChange(asunto: String) {
        _uiState.update {
            it.copy(
                asunto = asunto,
                errorMessage = if (asunto.isBlank()) "Debes rellenar el campo Asunto"
                else null
            )
        }
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

    fun onPrioridadChange(prioridadId: Int) {
        _uiState.update {
            it.copy(
                prioridadId = prioridadId,
                errorMessage = if (prioridadId == 0) "Debes rellenar el campo Prioridad"
                else null
            )
        }
    }

    fun onFechaChange(fecha: String) {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        _uiState.update {
            it.copy(
                fecha = dateFormatter.parse(fecha),
                errorMessage = if (fecha.isBlank()) "Debes rellenar el campo Fecha"
                else null
            )
        }
    }

    fun onTecnicoChange(tecnicoId: Int) {
        _uiState.update {
            it.copy(
                tecnicoId = tecnicoId,
                errorMessage = if (tecnicoId == 0) "Debes rellenar el campo Tecnico"
                else null
            )
        }
    }

    fun onClienteChange(cliente: String) {
        _uiState.update {
            it.copy(
                cliente = cliente,
                errorMessage = if (cliente.isBlank()) "Debes rellenar el campo Cliente"
                else null
            )
        }
    }

    fun new() {
        _uiState.value = TicketUiState()
    }

    fun delete() {
        viewModelScope.launch {
            ticketRepository.delete(uiState.value.toEntity())
        }
    }

    private fun getTickets() {
        viewModelScope.launch {
            ticketRepository.getAll().collect { tickets ->
                _uiState.update {
                    it.copy(tickets = tickets)
                }
            }
        }
    }

    private fun getTecnicos() {
        viewModelScope.launch {
            tecnicoRepository.getAll().collect { tecnicos ->
                _tecnicosList.value = tecnicos
            }
        }
    }

    fun find(ticketId: Int) {
        viewModelScope.launch {
            if (ticketId > 0) {
                val ticket = ticketRepository.find(ticketId)
                if (ticket != null) {
                    _uiState.update {
                        it.copy(
                            ticketId = ticket.ticketId,
                            fecha = ticket.fecha,
                            cliente = ticket.cliente,
                            asunto = ticket.asunto,
                            descripcion = ticket.descripcion,
                            prioridadId = ticket.prioridadId,
                            tecnicoId = ticket.tecnicoId
                        )
                    }
                }
            }
        }
    }

    fun isValid(): Boolean {
        val asuntoValid = _uiState.value.asunto.isNotBlank()
        val descripcionValid = _uiState.value.descripcion.isNotBlank()
        val prioridadValid = _uiState.value.prioridadId != null
        val clienteValid = _uiState.value.cliente.isNotBlank()
        val tecnicoValid = _uiState.value.tecnicoId != null
        val fechaValid = _uiState.value.fecha != null

        _uiState.update {
            it.copy(
                errorMessage = when {
                    !asuntoValid -> "Debes rellenar el campo Asunto"
                    !descripcionValid -> "Debes rellenar el campo Descripción"
                    !prioridadValid -> "Debes seleccionar una Prioridad"
                    !clienteValid -> "Debes rellenar el campo Cliente"
                    !tecnicoValid -> "Debes seleccionar un Técnico"
                    !fechaValid -> "Debes seleccionar una Fecha"
                    else -> null
                }
            )
        }

        return asuntoValid && descripcionValid && prioridadValid && clienteValid && tecnicoValid && fechaValid
    }
}

fun TicketUiState.toEntity() = TicketEntity(
    ticketId = this.ticketId,
    fecha = this.fecha,
    prioridadId = this.prioridadId,
    cliente = this.cliente,
    asunto = this.asunto,
    descripcion = this.descripcion,
    tecnicoId = this.tecnicoId
)