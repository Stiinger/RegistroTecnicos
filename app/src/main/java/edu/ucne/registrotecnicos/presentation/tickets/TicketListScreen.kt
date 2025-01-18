package edu.ucne.registrotecnicos.presentation.tickets

import androidx.compose.foundation.clickable
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
import edu.ucne.registrotecnicos.data.local.entities.TicketEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    ticketList: List<TicketEntity>,
    createTicket: () -> Unit,
    goToMenu: () -> Unit,
    goToTicket: (Int) -> Unit
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
                        text = "Lista de Tickets",
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
                    TicketHeaderRow()
                }
                items(ticketList) { ticket ->
                    TicketRow(
                        ticket = ticket,
                        goToTicket = { ticket.ticketId?.let { it1 -> goToTicket(it1) } }
                    )
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
            onClick = { createTicket() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text(text = "Nuevo Ticket")
        }
    }
}

@Composable
private fun TicketHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(modifier = Modifier.weight(1f), text = "ID", style = MaterialTheme.typography.bodyLarge)
        Text(modifier = Modifier.weight(1f), text = "Prioridad", style = MaterialTheme.typography.bodyLarge)
        Text(modifier = Modifier.weight(1f), text = "Asunto", style = MaterialTheme.typography.bodyLarge)
        Text(modifier = Modifier.weight(1f), text = "Cliente", style = MaterialTheme.typography.bodyLarge)
    }
    HorizontalDivider()
}

@Composable
private fun TicketRow(
    ticket: TicketEntity,
    goToTicket: (Int) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                ticket.ticketId?.let { goToTicket(it)}
            }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(modifier = Modifier.weight(1f), text = ticket.ticketId.toString())
        Text(modifier = Modifier.weight(1f), text = ticket.prioridadId.toString())
        Text(modifier = Modifier.weight(1f), text = ticket.asunto)
        Text(modifier = Modifier.weight(1f), text = ticket.cliente)
    }
    HorizontalDivider()
}