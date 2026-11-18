package com.example.filmlistan

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.filmlistan.ui.theme.FilmlistanTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FilmlistanTheme {
                FilmScreen()
                }
            }
        }
    }


data class Film(
    val id: Int,
    val titel: String,
    val ar: Int,
    val genre: String,
    val sedd: Boolean = false,
)

val startfilmer = listOf(
    Film(1, "Norrskensväktaren", 2024, "Äventyr"),
    Film(2, "Den sista bussen", 2021, "Drama"),
    Film(3, "Kodnamn Granit", 2023, "Thriller"),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilmScreen(
    viewModel: FilmlistanViewModel = viewModel()
) {
    val uiState = viewModel.uiState
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Filmlistan",
                        color = Color.White
                    )

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF6750A4)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            OutlinedTextField(
                value = uiState.nyTitel,
                onValueChange = { nyText ->
                    viewModel.uppdateraTitel(nyText)
                },
                label = { Text("Filmens titel") }
            )
            Button(
                onClick = {
                    viewModel.laggTillFilm()
                },
                enabled = uiState.nyTitel.isNotBlank()
            ) {
                Text("Lägg till")
            }
            LazyColumn {

                item {
                    Text("Vill se")
                }

                items(
                    uiState.filmer.filter { !it.sedd },
                    key = { it.id }
                ) { film ->
                    FilmCard(
                        film = film,
                        onSeddChange = {id ->
                            viewModel.andraSedd(id)
                        },
                        onTaBort = {id ->
                            viewModel.taBortFilm(id)
                        }
                        )
                }
                item {
                    Text("Sett")
                }
                items(
                    uiState.filmer.filter {it.sedd},
                    key = {it.id}
                ) { film ->
                    FilmCard(
                        film = film,
                        onSeddChange = {id ->
                            viewModel.andraSedd(id)
                        },
                        onTaBort = {id ->
                            viewModel.taBortFilm(id)
                        }
                    )
                }
            }
        }

    }
}

@Composable
fun FilmCard(
    film: Film,
    onSeddChange: (Int) -> Unit,
    onTaBort: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
        ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(Color(0xFF6750A4)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = film.titel.first().toString(),
                    color = Color.White
                    )
            }
            Column {
                Text(film.titel)
                Text(film.ar.toString())
                Text(film.genre)
            }
            TextButton(
                onClick = {
                    onTaBort(film.id)
                }
            ) {
                Text("Ta bort")
            }
            Checkbox(
                checked = film.sedd,
                onCheckedChange = { onSeddChange(film.id) }
            )
        }
    }

}