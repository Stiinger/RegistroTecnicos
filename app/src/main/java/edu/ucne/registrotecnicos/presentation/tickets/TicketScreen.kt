package edu.ucne.registrotecnicos.presentation.tickets

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity
import edu.ucne.registrotecnicos.data.repository.TicketRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    goBackToList: () -> Unit,
    ticketRepository: TicketRepository,
    tecnicosList: List<TecnicoEntity>,
    ticketId: Int
) {
    var fecha by remember { mutableStateOf("") }
    var prioridadSeleccionada by remember { mutableStateOf("") }
    var cliente by remember { mutableStateOf("") }
    var asunto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tecnicoSeleccionado by remember { mutableStateOf<TecnicoEntity?>(null) }
    var errorMessage: String? by remember { mutableStateOf(null) }
    val calendar = remember { Calendar.getInstance() }
    val context = LocalContext.current

    LaunchedEffect(ticketId) {
        if (ticketId > 0) {
            val ticket = ticketRepository.find(ticketId)
            ticket?.let {
                val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                fecha = dateFormat.format(it.fecha!!)
                prioridadSeleccionada = it.prioridadId.toString()
                cliente = it.cliente
                asunto = it.asunto
                descripcion = it.descripcion
                tecnicoSeleccionado =
                    tecnicosList.find { tecnico -> tecnico.tecnicoId == it.tecnicoId }
            }
        }
    }

    Scaffold(
        topBar = {
            androidx.compose.material3.CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = if (ticketId > 0) "Editar Ticket" else "Registro de Ticket",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
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
                        value = asunto,
                        onValueChange = { asunto = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        maxLines = 3,
                        singleLine = false
                    )
                    OutlinedTextField(
                        label = { Text(text = "Fecha") },
                        value = fecha,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Seleccionar Fecha",
                                modifier = Modifier.clickable {
                                    val datePicker = DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            calendar.set(year, month, dayOfMonth)
                                            val formattedDate =
                                                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
                                                    .format(calendar.time)
                                            fecha = formattedDate
                                        },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    )
                                    datePicker.show()
                                }
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
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
                            value = prioridadSeleccionada,
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
                                        prioridadSeleccionada = numero.toString()
                                        expandedPrioridad = false
                                    }
                                )
                            }
                        }
                    }
                    OutlinedTextField(
                        label = { Text(text = "Cliente") },
                        value = cliente,
                        onValueChange = { cliente = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    var expandedTecnico by remember { mutableStateOf(false) }
                    ExposedDropdownMenuBox(
                        expanded = expandedTecnico,
                        onExpandedChange = { expandedTecnico = !expandedTecnico }
                    ) {
                        OutlinedTextField(
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            readOnly = true,
                            value = tecnicoSeleccionado?.nombres ?: "",
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
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(2.dp))
                    errorMessage?.let {
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
                        val scope = rememberCoroutineScope()
                        OutlinedButton(onClick = {
                            if (ticketId > 0) {
                                scope.launch {
                                    ticketRepository.delete(
                                        TicketEntity(
                                            ticketId = ticketId,
                                            fecha = Date(),
                                            prioridadId = 0,
                                            cliente = "",
                                            asunto = "",
                                            descripcion = "",
                                            tecnicoId = 0
                                        )
                                    )
                                }
                            }
                            prioridadSeleccionada = ""
                            cliente = ""
                            asunto = ""
                            descripcion = ""
                            tecnicoSeleccionado = null
                            errorMessage = ""
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
                                if (asunto.isBlank() || descripcion.isBlank()
                                    || prioridadSeleccionada.isBlank()
                                    || cliente.isBlank() || tecnicoSeleccionado == null
                                ) {
                                    errorMessage = "No se puede guardar con datos vacíos"
                                    return@OutlinedButton
                                }
                                val fechaFormateada =
                                    SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).parse(fecha)
                                scope.launch {
                                    ticketRepository.save(
                                        TicketEntity(
                                            ticketId = if (ticketId > 0) ticketId else null,
                                            fecha = fechaFormateada,
                                            prioridadId = prioridadSeleccionada.toInt(),
                                            cliente = cliente,
                                            asunto = asunto,
                                            descripcion = descripcion,
                                            tecnicoId = tecnicoSeleccionado!!.tecnicoId ?: 0
                                        )
                                    )
                                }
                                goBackToList()
                            }) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Save button"
                            )
                            Text(text = "Guardar")
                        }
                    }
                }
            }
        }
    }
}