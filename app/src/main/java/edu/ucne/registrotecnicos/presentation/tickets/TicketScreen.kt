package edu.ucne.registrotecnicos.presentation.tickets

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.presentation.components.ConfirmDeletionDialog
import edu.ucne.registrotecnicos.presentation.components.CustomDatePicker
import edu.ucne.registrotecnicos.presentation.components.CustomDropdownMenu
import edu.ucne.registrotecnicos.presentation.components.TopBar
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TicketScreen(
    viewModel: TicketViewModel = hiltViewModel(),
    ticketId: Int,
    goBackToList: () -> Unit,
    goToReply: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tecnicosList by viewModel.tecnicosList.collectAsStateWithLifecycle()
    TicketBodyScreen(
        ticketId = ticketId,
        viewModel = viewModel,
        uiState = uiState,
        tecnicosList = tecnicosList,
        goBackToList = goBackToList,
        goToReply = goToReply
    )
}

@Composable
fun TicketBodyScreen(
    ticketId: Int,
    viewModel: TicketViewModel,
    uiState: TicketUiState,
    tecnicosList: List<TecnicoEntity>,
    goBackToList: () -> Unit,
    goToReply: () -> Unit
) {
    LaunchedEffect(ticketId) {
        if (ticketId > 0) {
            viewModel.find(ticketId)
        }
    }
    val openDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopBar(if (ticketId > 0) "Editar Ticket" else "Registrar Ticket")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        label = { Text(text = "Asunto") },
                        value = uiState.asunto,
                        onValueChange = viewModel::onAsuntoChange,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = uiState.descripcion,
                        onValueChange = viewModel::onDescripcionChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        maxLines = 3,
                        singleLine = false
                    )
                    val formattedDate = uiState.fecha?.let {
                        SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it)
                    } ?: ""
                    CustomDatePicker(
                        label = "Fecha",
                        selectedDate = formattedDate,
                        onDateSelected = { date -> viewModel.onFechaChange(date) }
                    )
                    val prioridades = listOf(1, 2, 3)
                    var expandedPrioridad by remember { mutableStateOf(false) }
                    CustomDropdownMenu(
                        label = "Prioridad",
                        selectedItem = uiState.prioridadId?.toString() ?: "Seleccionar Prioridad",
                        items = prioridades,
                        onItemSelected = { numero -> viewModel.onPrioridadChange(numero) },
                        expanded = expandedPrioridad,
                        onExpandedChange = { expandedPrioridad = it },
                        itemText = { it.toString() }
                    )
                    OutlinedTextField(
                        label = { Text(text = "Cliente") },
                        value = uiState.cliente,
                        onValueChange = viewModel::onClienteChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                    var tecnicoSeleccionado by remember { mutableStateOf<TecnicoEntity?>(null) }
                    var expandedTecnico by remember { mutableStateOf(false) }

                    LaunchedEffect(uiState.tecnicoId) {
                        val tecnico = tecnicosList.find { it.tecnicoId == uiState.tecnicoId }
                        tecnicoSeleccionado = tecnico
                    }
                    CustomDropdownMenu(
                        label = "Técnico",
                        selectedItem = tecnicoSeleccionado?.nombres ?: "Seleccionar Técnico",
                        items = tecnicosList,
                        onItemSelected = { tecnico ->
                            tecnicoSeleccionado = tecnico
                            tecnico.tecnicoId?.let { viewModel.onTecnicoChange(it) }
                        },
                        expanded = expandedTecnico,
                        onExpandedChange = { expandedTecnico = it },
                        itemText = { it.nombres }
                    )
                }
                Spacer(modifier = Modifier.padding(2.dp))
                uiState.errorMessage?.let {
                    Text(text = it, color = Color.Red)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(onClick = { goBackToList() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back"
                        )
                        Text(text = "Atrás")
                    }

                    OutlinedButton(onClick = {
                        if (ticketId > 0) {
                            openDialog.value = true
                        } else {
                            viewModel.new()
                            goBackToList()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = if (ticketId > 0) "Borrar" else "Limpiar"
                        )
                        Text(text = if (ticketId > 0) "Borrar" else "Limpiar")
                    }

                    OutlinedButton(
                        onClick = {
                            if (viewModel.isValid()) {
                                viewModel.save()
                                goBackToList()
                            }
                        }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Save button"
                        )
                        Text(text = "Guardar")
                    }
                }

                ConfirmDeletionDialog(
                    openDialog = openDialog,
                    onConfirm = {
                        viewModel.delete()
                        goBackToList()
                    },
                    onDismiss = {
                        openDialog.value = false
                    }
                )
                if (ticketId > 0) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedButton(
                            onClick = {
                                goToReply()
                            },
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Reply button"
                            )
                            Text(
                                text = "Responder Ticket",
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}