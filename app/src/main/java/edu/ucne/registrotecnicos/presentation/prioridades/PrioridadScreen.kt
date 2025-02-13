package edu.ucne.registrotecnicos.presentation.prioridades

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.presentation.components.ConfirmDeletionDialog
import edu.ucne.registrotecnicos.presentation.components.TopBar

@Composable
fun PrioridadScreen(
    viewModel: PrioridadViewModel = hiltViewModel(),
    prioridadId: Int,
    goBackToList: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    PrioridadBodyScreen(
        prioridadId = prioridadId,
        viewModel = viewModel,
        uiState = uiState,
        goBackToList = goBackToList
    )
}

@Composable
fun PrioridadBodyScreen(
    prioridadId: Int,
    viewModel: PrioridadViewModel,
    uiState: PrioridadUiState,
    goBackToList: () -> Unit
) {
    LaunchedEffect(prioridadId) {
        if (prioridadId > 0) viewModel.find(prioridadId)
    }
    val openDialog = remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopBar(if (prioridadId > 0) "Editar Prioridad" else "Registrar Prioridad")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        label = { Text(text = "Descripción") },
                        value = uiState.descripcion,
                        onValueChange = viewModel::onDescripcionChange,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    OutlinedTextField(
                        label = { Text(text = "Días Compromiso") },
                        value = uiState.diasCompromiso,
                        onValueChange = viewModel::onDiasCompromisoChange,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            unfocusedLabelColor = MaterialTheme.colorScheme.onSurface
                        )
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    uiState.errorMessage?.let {
                        Text(text = it, color = MaterialTheme.colorScheme.error)
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
                            if (prioridadId > 0) {
                                openDialog.value = true
                            } else {
                                viewModel.new()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = if (prioridadId > 0) "Borrar" else "Limpiar"
                            )
                            Text(text = if (prioridadId > 0) "Borrar" else "Limpiar")
                        }
                        OutlinedButton(
                            onClick = {
                                if (viewModel.isValid()) {
                                    if (prioridadId > 0) viewModel.update() else viewModel.save()
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
                }
            }
        }
    }
    ConfirmDeletionDialog(
        openDialog = openDialog,
        onConfirm = {
            viewModel.delete(uiState.prioridadId)
            goBackToList()
        },
        onDismiss = {
            openDialog.value = false
        }
    )
}