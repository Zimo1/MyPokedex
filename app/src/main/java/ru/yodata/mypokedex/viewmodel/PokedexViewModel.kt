package ru.yodata.mypokedex.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import ru.yodata.mypokedex.model.room.entity.PokeCard
import ru.yodata.mypokedex.view.pokeDB

// ViewModel для MVVM
class PokedexViewModel(application: Application): AndroidViewModel(application) {
    internal val pokedex : LiveData<List<PokeCard>> = pokeDB!!.pokeDao().pokeGetAll()
}