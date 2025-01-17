package edu.ucne.registrotecnicos.presentation.tecnicos

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoScreen(
    goBackToList: () -> Unit,
    tecnicoRepository: TecnicoRepository
) {
    var nombres by remember { mutableStateOf("") }
    var sueldo by remember { mutableStateOf("") }
    var errorMessage: String? by remember { mutableStateOf(null) }

    Scaffold(
        topBar = {
            androidx.compose.material3.CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Registro de Técnicos",
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
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    OutlinedTextField(
                        label = { Text(text = "Nombres") },
                        value = nombres,
                        onValueChange = { nombres = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        label = { Text(text = "Sueldo") },
                        value = sueldo,
                        onValueChange = { sueldo = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                    errorMessage?.let {
                        Text(text = it, color = Color.Red)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(onClick = {
                            goBackToList()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Go back"
                            )
                            Text(text = "Atrás")
                        }
                        OutlinedButton(onClick = {
                            nombres = ""
                            sueldo = ""
                            errorMessage = ""
                        }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "New button"
                            )
                            Text(text = "Limpiar")
                        }
                        val scope = rememberCoroutineScope()
                        OutlinedButton(
                            onClick = {
                                if (nombres.isBlank() || sueldo.isEmpty()) {
                                    errorMessage = "No se puede guardar con datos vacíos"
                                    return@OutlinedButton
                                }
                                if (sueldo.toDouble() <= 1.0) {
                                    errorMessage = "El sueldo debe ser mayor que 1.0"
                                    return@OutlinedButton
                                }
                                    scope.launch {
                                        tecnicoRepository.save(
                                            TecnicoEntity(
                                                nombres = nombres,
                                                sueldo = sueldo.toDouble()
                                            )
                                        )
                                        nombres = ""
                                        sueldo = ""
                                        errorMessage = ""
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