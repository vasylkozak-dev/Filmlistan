package com.example.filmlistan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class FilmlistanUiState(
    val filmer: List<Film> = startfilmer,
    val nyTitel: String = ""
)


class FilmlistanViewModel : ViewModel() {
    private var nextId = 4
    var uiState by mutableStateOf(FilmlistanUiState())
        private set

    fun uppdateraTitel(titel: String) {
        uiState = uiState.copy(nyTitel = titel)
    }

    fun laggTillFilm() {
        if (uiState.nyTitel.isBlank()) return

        val nyFilm = Film(
            id = nextId++,
            titel = uiState.nyTitel.trim(),
            ar = 2024,
            genre = "Okänd"
        )
        uiState = uiState.copy(
            filmer = uiState.filmer + nyFilm,
            nyTitel = ""
        )
    }

    fun andraSedd(id: Int) {
        uiState = uiState.copy(
            filmer = uiState.filmer.map { film ->
                if (film.id == id) {
                    film.copy(sedd = !film.sedd)
                } else {
                    film
                }
            }
        )
    }

    fun taBortFilm(id: Int) {
        uiState = uiState.copy(
            filmer = uiState.filmer.filter { it.id != id }
        )
    }
}