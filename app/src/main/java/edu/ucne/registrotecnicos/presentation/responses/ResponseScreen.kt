package edu.ucne.registrotecnicos.presentation.responses

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.presentation.components.TopBar

@Composable
fun ResponseScreen(
    viewModel: ResponseViewModel = hiltViewModel(),
    ticketId: Int,
    goBackToTicket: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ResponseBodyScreen(
        ticketId = ticketId,
        viewModel,
        uiState = uiState,
        goBackToTicket = goBackToTicket
    )
}

@Composable
fun ResponseBodyScreen(
    ticketId: Int,
    viewModel: ResponseViewModel,
    uiState: ResponseUiState,
    goBackToTicket: () -> Unit
) {
    LaunchedEffect(ticketId) {
        if (ticketId > 0) viewModel.loadResponses(ticketId)
    }
    Scaffold(
        topBar = {
            TopBar("Responder Ticket")
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(8.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                reverseLayout = true
            ) {
                items(uiState.mensajes) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium)
                            .padding(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Posted by ",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        style = TextStyle(fontSize = 14.sp)
                                    )
                                    Text(
                                        text = it.nombreTecnico,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = " on ${it.fechaHora}",
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                                        style = TextStyle(fontSize = 14.sp)
                                    )
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .background(Color(0xFF4CAF50))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = "Técnico",
                                        color = Color.White,
                                        style = TextStyle(
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.padding(4.dp))
                            Text(
                                text = it.contenido,
                                color = MaterialTheme.colorScheme.onSurface,
                                style = TextStyle(fontSize = 16.sp)
                            )
                        }
                    }
                }
            }
            var showResponseForm by rememberSaveable { mutableStateOf(false) }
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedButton(
                onClick = { showResponseForm = !showResponseForm },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(if(showResponseForm) "Ocultar Formulario" else "Responder Ticket")
            }
            if (showResponseForm) {
                Spacer(modifier = Modifier.height(8.dp))
                ElevatedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color.Gray, shape = MaterialTheme.shapes.medium),
                    elevation = CardDefaults.elevatedCardElevation(4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Responder",
                            style = TextStyle(
                                color = Color.Gray,
                                fontSize = 18.sp
                            ),
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        OutlinedTextField(
                            value = uiState.nombreTecnico,
                            onValueChange = {},
                            label = { Text("Técnico") },
                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                        var respuesta by remember { mutableStateOf(TextFieldValue("")) }
                        OutlinedTextField(
                            value = respuesta,
                            onValueChange = { respuesta = it },
                            label = { Text("Escribe tu respuesta aquí...") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                                .heightIn(min = 100.dp),
                            maxLines = 5,
                            singleLine = false
                        )
                        Text(
                            text = "Words: ${respuesta.text.split("\\s+".toRegex()).size}", // contar palabras
                            style = TextStyle(color = Color.Gray),
                            modifier = Modifier.align(Alignment.End)
                        )
                        Spacer(modifier = Modifier.padding(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(onClick = { goBackToTicket() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Atrás"
                                )
                                Text(text = "Atrás")
                            }
                            OutlinedButton(onClick = {
                                if (respuesta.text.isNotBlank()) {
                                    viewModel.sendResponse(respuesta.text)
                                    respuesta = TextFieldValue("")
                                }
                            }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Send,
                                    contentDescription = "Enviar"
                                )
                                Text(text = "Enviar")
                            }
                        }
                    }
                }
            }
        }
    }
}