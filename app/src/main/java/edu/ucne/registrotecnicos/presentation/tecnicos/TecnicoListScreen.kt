package edu.ucne.registrotecnicos.presentation.tecnicos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecnicoListScreen(
    tecnicoList: List<TecnicoEntity>,
    createTecnico: () -> Unit,
    goToMenu: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            androidx.compose.material3.CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Lista de Técnicos",
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                colors = androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    TecnicoHeaderRow()
                }
                items(tecnicoList) { tecnico ->
                    TecnicoRow(tecnico)
                }
            }
        }
        OutlinedButton(
            onClick = { goToMenu() },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(text = "Menú Principal")
        }
        OutlinedButton(
            onClick = { createTecnico() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(text = "Nuevo Técnico")
        }
    }
}

@Composable
private fun TecnicoHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(modifier = Modifier.weight(1f), text = "ID", style = MaterialTheme.typography.bodyLarge)
        Text(modifier = Modifier.weight(2f), text = "Nombre", style = MaterialTheme.typography.bodyLarge)
        Text(modifier = Modifier.weight(1f), text = "Sueldo", style = MaterialTheme.typography.bodyLarge)
    }
    HorizontalDivider()
}

@Composable
private fun TecnicoRow(it: TecnicoEntity) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(modifier = Modifier.weight(1f), text = it.tecnicoId.toString())
        Text(
            modifier = Modifier.weight(2f),
            text = it.nombres,
            style = MaterialTheme.typography.bodyMedium
        )
        Text(modifier = Modifier.weight(1f), text = it.sueldo.toString())
    }
    HorizontalDivider()
}