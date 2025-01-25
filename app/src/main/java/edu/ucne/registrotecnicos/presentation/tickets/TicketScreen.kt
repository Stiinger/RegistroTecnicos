package edu.ucne.registrotecnicos.presentation.tickets

import android.app.DatePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.presentation.components.TopBar
import java.text.SimpleDateFormat
import java.util.Calendar
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
        viewModel,
        uiState = uiState,
        tecnicosList = tecnicosList,
        goBackToList,
        goToReply
    )
}

@OptIn(ExperimentalMaterial3Api::class)
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
                    val context = LocalContext.current
                    var showDatePicker by remember { mutableStateOf(false) }
                    val calendar = Calendar.getInstance()
                    val dateFormatter =
                        remember { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) }
                    val formattedDate =
                        viewModel.uiState.value.fecha?.let { dateFormatter.format(it) } ?: ""
                    if (showDatePicker) {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth)
                                viewModel.onFechaChange(dateFormatter.format(calendar.time))
                                showDatePicker = false
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                    OutlinedTextField(
                        label = { Text("Fecha") },
                        value = formattedDate,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(
                                    Icons.Default.DateRange,
                                    contentDescription = "Seleccionar Fecha"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                    )
                    val prioridades = listOf(1, 2, 3)
                    var expandedPrioridad by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedPrioridad,
                        onExpandedChange = { expandedPrioridad = !expandedPrioridad }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            value = uiState.prioridadId?.toString() ?: "Seleccionar Prioridad",
                            onValueChange = {},
                            label = { Text("Prioridad") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedPrioridad) }
                        )
                        ExposedDropdownMenu(
                            expanded = expandedPrioridad,
                            onDismissRequest = { expandedPrioridad = false }
                        ) {
                            prioridades.forEach { numero ->
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text(text = numero.toString()) },
                                    onClick = {
                                        expandedPrioridad = false
                                        viewModel.onPrioridadChange(numero)
                                    }
                                )
                            }
                        }
                    }
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
                    ExposedDropdownMenuBox(
                        expanded = expandedTecnico,
                        onExpandedChange = { expandedTecnico = !expandedTecnico }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            value = tecnicoSeleccionado?.nombres ?: "Seleccionar Técnico",
                            onValueChange = {},
                            label = { Text("Técnico") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTecnico) }
                        )
                        ExposedDropdownMenu(
                            expanded = expandedTecnico,
                            onDismissRequest = { expandedTecnico = false }
                        ) {
                            tecnicosList.forEach { tecnico ->
                                androidx.compose.material3.DropdownMenuItem(
                                    text = { Text(text = tecnico.nombres) },
                                    onClick = {
                                        tecnicoSeleccionado = tecnico
                                        expandedTecnico = false
                                        tecnico.tecnicoId?.let { viewModel.onTecnicoChange(it) }
                                    }
                                )
                            }
                        }
                    }
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
                        if (ticketId > 0)
                            viewModel.delete()
                        else
                            viewModel.new()
                        goBackToList()
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
                if(ticketId > 0)
                {
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