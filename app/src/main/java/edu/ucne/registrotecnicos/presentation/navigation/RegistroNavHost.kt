package edu.ucne.registrotecnicos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.data.repository.TicketRepository
import edu.ucne.registrotecnicos.presentation.home.HomeScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketListScreen
import edu.ucne.registrotecnicos.presentation.tickets.TicketScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun RegistroNavHost(
    navHostController: NavHostController,
    tecnicoRepository: TecnicoRepository,
    ticketRepository: TicketRepository
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val tecnicoList by tecnicoRepository.getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )
    val ticketList by ticketRepository.getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )
    NavHost(
        navController = navHostController,
        startDestination = Screen.Home
    ) {
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                tecnicoList = tecnicoList,
                createTecnico = { navHostController.navigate(Screen.Tecnico(0)) },
                goToMenu = { navHostController.navigateUp() }
            )
        }
        composable<Screen.Tecnico> {
            TecnicoScreen(
                tecnicoRepository = tecnicoRepository,
                goBackToList = { navHostController.navigateUp() }
            )
        }
        composable<Screen.TicketList> {
            TicketListScreen(
                ticketList = ticketList,
                createTicket = { navHostController.navigate(Screen.Ticket(0)) },
                goToMenu = { navHostController.navigateUp() },
                goToTicket = {
                    navHostController.navigate(Screen.Ticket(it))
                }
            )
        }
        composable<Screen.Ticket> {
            val ticketId = it.toRoute<Screen.Ticket>().ticketId
            TicketScreen(
                ticketRepository = ticketRepository,
                goBackToList = { navHostController.navigateUp() },
                tecnicosList = tecnicoList,
                ticketId = ticketId
            )
        }
        composable<Screen.Home> {
            HomeScreen(
                goToTickets = { navHostController.navigate(Screen.TicketList) },
                goToTecnicos = { navHostController.navigate(Screen.TecnicoList) }
            )
        }
    }
}