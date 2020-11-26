package ru.yodata.mypokedex.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.pokecard.*
import kotlinx.android.synthetic.main.pokedex_fragment.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.yodata.mypokedex.R
import ru.yodata.mypokedex.viewmodel.PokeRecyclerViewAdapter
import ru.yodata.mypokedex.viewmodel.PokedexViewModel

// Фрагмент Избранное.
// Список с данными и аватарами покемонов отображается в RecyclerView по MVVM
class PokedexFragment : Fragment() {

    companion object {
        fun newInstance() = PokedexFragment()
    }

    private lateinit var viewModel: PokedexViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pokedex_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Подключение к данным
        viewModel = ViewModelProviders.of(this).get(PokedexViewModel::class.java)
        pokedexRecyclerView.layoutManager = LinearLayoutManager(activity)
        // Подписка на viewmodel
        viewModel.pokedex.observe(viewLifecycleOwner, Observer{ pokemons->
            // Привязка данных RecyclerView
            pokedexRecyclerView.adapter = PokeRecyclerViewAdapter(pokemons)
        })
        // Подключение обработчика кнопки удаления всех покемонов из Избранного
        pokesDeleteButton.setOnClickListener { onPokemonsDeleteButtonClick() }
    }

    fun onPokemonsDeleteButtonClick() {
        GlobalScope.launch {
            pokeDB!!.pokeDao().pokeDeleteAll()
            Log.d(TAG, "Удаление всех записей БД")
        }
    }

}