package edu.ucne.registrotecnicos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import edu.ucne.registrotecnicos.data.local.database.RegistroDb
import edu.ucne.registrotecnicos.data.local.entities.TecnicoEntity
import edu.ucne.registrotecnicos.data.repository.TecnicoRepository
import edu.ucne.registrotecnicos.data.repository.TicketRepository
import edu.ucne.registrotecnicos.presentation.navigation.RegistroNavHost
import edu.ucne.registrotecnicos.ui.theme.RegistroTecnicosTheme

class MainActivity : ComponentActivity() {
    private lateinit var tecnicoRepository: TecnicoRepository
    private lateinit var ticketRepository: TicketRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val registroDb = Room.databaseBuilder(
            applicationContext,
            RegistroDb::class.java,
            "Registro.db"
        ).fallbackToDestructiveMigration()
            .build()
        tecnicoRepository = TecnicoRepository(registroDb)
        ticketRepository = TicketRepository(registroDb)

        setContent {
            RegistroTecnicosTheme {
                val navHost = rememberNavController()
                RegistroNavHost(navHost, tecnicoRepository, ticketRepository)
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