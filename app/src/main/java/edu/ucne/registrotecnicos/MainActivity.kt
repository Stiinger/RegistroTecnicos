package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.ucne.registrotecnicos.data.local.database.TecnicoDb
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.presentation.navigation.TecnicosNavHost
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoRepository: TecnicoRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val tecnicoDb = Room.databaseBuilder(
            applicationContext,
            TecnicoDb::class.java,
            "Tecnico.db"
        ).fallbackToDestructiveMigration()
            .build()
        tecnicoRepository = TecnicoRepository(tecnicoDb)

        setContent {
            RegistroTecnicosTheme {
                val navHost = rememberNavController()
                TecnicosNavHost(navHost, tecnicoRepository)
            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun GreetingPreview() {
        RegistroTecnicosTheme {
            val tecnicoList = listOf(
                TecnicoEntity(1, "Jose", 99.99),
            )
        }
    }
}