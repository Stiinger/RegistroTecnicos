package edu.ucne.registrotecnicos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.presentation.home.HomeScreen
import edu.ucne.registrotecnicos.presentation.prioridades.PrioridadListScreen
import edu.ucne.registrotecnicos.presentation.prioridades.PrioridadScreen
import edu.ucne.registrotecnicos.presentation.responses.ResponseScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketListScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketScreen

@Composable
fun RegistroNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home
    ) {
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                createTecnico = { navHostController.navigate(Screen.Tecnico(0)) },
                goToMenu = { navHostController.navigateUp() },
                goToTecnico = { tecnicoId ->
                    navHostController.navigate(Screen.Tecnico(tecnicoId = tecnicoId))
                }
            )
        }
        composable<Screen.Tecnico> {
            val tecnicoId = it.toRoute<Screen.Tecnico>().tecnicoId
            TecnicoScreen(
                tecnicoId = tecnicoId,
                goBackToList = { navHostController.navigateUp() }
            )
        }
        composable<Screen.TicketList> {
            TicketListScreen(
                createTicket = { navHostController.navigate(Screen.Ticket(0)) },
                goToMenu = { navHostController.navigateUp() },
                goToTicket = { navHostController.navigate(Screen.Ticket(it)) }
            )
        }
        composable<Screen.Ticket> {
            val ticketId = it.toRoute<Screen.Ticket>().ticketId
            TicketScreen(
                ticketId = ticketId,
                goBackToList = { navHostController.navigateUp() },
                goToReply = { navHostController.navigate(Screen.TicketResponse(ticketId)) }
            )
        }
        composable<Screen.Home> {
            HomeScreen(
                goToTickets = { navHostController.navigate(Screen.TicketList) },
                goToTecnicos = { navHostController.navigate(Screen.TecnicoList) },
                goToPrioridades = { navHostController.navigate(Screen.PrioridadList) }
            )
        }
        composable<Screen.TicketResponse> {
            val ticketId = it.toRoute<Screen.Ticket>().ticketId
            ResponseScreen(
                ticketId = ticketId,
                goBackToTicket = { navHostController.navigateUp() }
            )
        }
        composable<Screen.PrioridadList> {
            PrioridadListScreen(
                createPrioridad = { navHostController.navigate(Screen.Prioridad(0)) },
                goToMenu = { navHostController.navigateUp() },
                goToPrioridad = { navHostController.navigate(Screen.Prioridad(it)) }
            )
        }
        composable<Screen.Prioridad> {
            val prioridadId = it.toRoute<Screen.Prioridad>().prioridadId
            PrioridadScreen(
                prioridadId = prioridadId,
                goBackToList = { navHostController.navigateUp() }
            )
        }
    }
}