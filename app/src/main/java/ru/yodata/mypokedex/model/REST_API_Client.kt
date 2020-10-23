package ru.yodata.mypokedex.model

import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.yodata.mypokedex.view.SERVER_URL

object REST_API_Client {
    private var retrofit =
        Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    private var api: PokeAPI =
        retrofit.create()

    fun buildService(): PokeAPI {
        return api
    }
    // Получение списка всех имеющихся покемонов
    fun getPokemonList() = api.getPokemonList()
    // Получение данных по конкретному покемону, на которого указывает ссылка pokeURL, взятая из
    // списка всех покемонов
    fun getPokemonData(pokeURL: String) = api.getPokemonData(pokeURL)
}