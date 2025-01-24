package edu.ucne.registrotecnicos.presentation.tecnicos

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.presentation.components.TopBar

@Composable
fun TecnicoListScreen(
    viewModel: TecnicoViewModel = hiltViewModel(),
    createTecnico: () -> Unit,
    goToMenu: () -> Unit,
    goToTecnico: (Int) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    TecnicoListBodyScreen(
        uiState,
        createTecnico,
        goToMenu,
        goToTecnico
    )
}

@Composable
fun TecnicoListBodyScreen(
    uiState: TecnicoUiState,
    createTecnico: () -> Unit,
    goToMenu: () -> Unit,
    goToTecnico: (Int) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            TopBar(
                titulo = "Lista de Técnicos",
                onBackClick = { goToMenu() },
                onCreateClick = { createTecnico() }
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    TecnicoHeaderRow()
                }
                items(uiState.tecnicos) {
                    TecnicoRow(
                        it,
                        goToTecnico
                    )
                }
            }
        }
    }
}

@Composable
private fun TecnicoHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.secondary)
            .padding(vertical = 12.dp)
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = "ID",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            modifier = Modifier.weight(2f),
            text = "Nombres",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            modifier = Modifier.weight(1f),
            text = "Sueldo",
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
    Divider(modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
private fun TecnicoRow(
    it: TecnicoEntity,
    goToTecnico: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                goToTecnico(it.tecnicoId!!)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = it.tecnicoId.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                modifier = Modifier.weight(2f),
                text = it.nombres,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                modifier = Modifier.weight(1f),
                text = "$${it.sueldo}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}