package edu.ucne.registrotecnicos.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoListScreen
import edu.ucne.registrotecnicos.presentation.tecnicos.TecnicoScreen

@Composable
fun TecnicosNavHost(
    navHostController: NavHostController,
    tecnicoRepository: TecnicoRepository
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val tecnicoList by tecnicoRepository.getAll()
        .collectAsStateWithLifecycle(
            initialValue = emptyList(),
            lifecycleOwner = lifecycleOwner,
            minActiveState = Lifecycle.State.STARTED
        )

    NavHost(
        navController = navHostController,
        startDestination = Screen.TecnicoList
    ) {
        composable<Screen.TecnicoList> {
            TecnicoListScreen(
                tecnicoList = tecnicoList,
                createTecnico = { navHostController.navigate(Screen.Tecnico(0)) },
            )
        }
        composable<Screen.Tecnico> {
            TecnicoScreen(
                tecnicoRepository = tecnicoRepository,
                goBackToList = { navHostController.navigateUp() }
            )
        }
    }
}