package edu.ucne.registrotecnicos.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    goToTickets: () -> Unit,
    goToTecnicos: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenido",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = { goToTickets() },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Ver Lista de Tickets")
        }

        Button(
            onClick = { goToTecnicos() },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = "Ver Lista de Técnicos")
        }
    }
}
