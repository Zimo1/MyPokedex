package ru.yodata.mypokedex.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.room.Room
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_intro.*
import kotlinx.android.synthetic.main.fragment_intro.view.*
import ru.yodata.mypokedex.R
import ru.yodata.mypokedex.model.REST_API_Client
import ru.yodata.mypokedex.model.pokemon_list_dto.PokemonList
import ru.yodata.mypokedex.model.room.database.PokeDataBase

// Фрагмент Заставка
class IntroFragment : Fragment() {
    private var disposableIntro: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Прочитать список имен покемонов с сервера REST API в pokemonList
        // Это делается только 1 раз за всё время работы приложения.
        if (pokemonList.isNullOrEmpty()) {
            disposableIntro = REST_API_Client.getPokemonList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({response ->
                    pokemonList = response.pokeList
                    Log.d(TAG, "Список покемонов получен с сервера REST")
                    //Инициализация базы данных
                    pokeDB = Room.databaseBuilder(
                        context?.applicationContext!!,
                        PokeDataBase::class.java, ROOM_DB_FILE_NAME
                    ).build()
                    startProgressBar.visibility = ProgressBar.GONE
                    // Переход на следующий экран по нажатию кнопки "Старт"
                    startBtn.setOnClickListener { view ->
                        view.findNavController().navigate(R.id.action_introFragment_to_menuFragment)}
                    startBtn.visibility = Button.VISIBLE
                }, this::handleError)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val introLayout = inflater.inflate(R.layout.fragment_intro, container, false)
        if (!pokemonList.isNullOrEmpty()) {
            with(introLayout) {
                startProgressBar.visibility = ProgressBar.GONE
                startBtn.visibility = Button.VISIBLE
                startBtn.setOnClickListener { view ->
                    view.findNavController().navigate(R.id.action_introFragment_to_menuFragment)
                }
            }
        }
        return introLayout
    }

    private fun handleResponse(response: PokemonList) {
        pokemonList = response.pokeList
        Log.d(TAG, "Список покемонов получен с сервера REST")
    }

    private fun handleError(error: Throwable) {
        Toast.makeText(context, "Ошибка REST: " + error.localizedMessage, Toast.LENGTH_SHORT).show()
        Log.d(TAG, "Ошибка REST: " + error.localizedMessage)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposableIntro?.dispose()
    }
}