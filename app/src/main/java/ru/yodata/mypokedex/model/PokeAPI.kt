package ru.yodata.mypokedex.model

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import ru.yodata.mypokedex.model.pokemon_dto.PokemonData
import ru.yodata.mypokedex.model.pokemon_list_dto.PokemonList
import io.reactivex.Observable

// Работа с сервером REST API https://pokeapi.co

interface PokeAPI {
    // Получение списка всех имеющихся покемонов
    @GET("pokemon?offset=0&limit=10000")
    fun getPokemonList(): Observable<PokemonList>

    // Получение данных по конкретному покемону, на которого указывает ссылка pokeurl, взятая из
    // списка всех покемонов
    @GET("{pokeurl}")
    fun getPokemonData(@Path("pokeurl") pokeurl: String): Observable<PokemonData>
}