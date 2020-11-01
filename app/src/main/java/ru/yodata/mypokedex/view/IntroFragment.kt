package ru.yodata.mypokedex.view

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.room.Room
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_intro.*
import kotlinx.android.synthetic.main.fragment_intro.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.yodata.mypokedex.R
import ru.yodata.mypokedex.model.REST_API_Client
import ru.yodata.mypokedex.model.pokemon_list_dto.PokemonList
import ru.yodata.mypokedex.model.room.database.PokeDataBase
import ru.yodata.mypokedex.model.room.entity.PokeCard

// Фрагмент Заставка
class IntroFragment : Fragment() {
    private var disposableIntro: Disposable? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Проверить подключение к Интернету
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
            // Соединение есть.
            this.arguments = bundleOf("isConnected" to true)
            // Прочитать список имен покемонов с сервера REST API в pokemonList
            // Это делается только 1 раз за всё время работы приложения.
            if (pokemonList.isNullOrEmpty()) {
                disposableIntro = REST_API_Client.getPokemonList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({ response ->
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
                            view.findNavController()
                                .navigate(R.id.action_introFragment_to_menuFragment)
                        }
                        startBtn.visibility = Button.VISIBLE
                    }, this::handleError)
            }
            /*// Предварительная загрузка аватаров в кеш Picasso - как выяснилось не нужна.
            //Инициализация базы данных
            GlobalScope.launch {
                pokeDB = Room.databaseBuilder(
                    context?.applicationContext!!,
                    PokeDataBase::class.java, ROOM_DB_FILE_NAME
                ).build()
                Log.d(TAG, "Загрузка аватаров покемонов в Избранном...")
                val pokedexList: List<PokeCard>? = pokeDB!!.pokeDao().pokeGetAllValue()
                if (!pokedexList.isNullOrEmpty()) {
                    for (pokemon in pokedexList) {
                        val picasso = Picasso.get()
                        //.with(context)
                        picasso.setIndicatorsEnabled(true)
                        picasso.load(pokemon.pokeAnimationURL ?: pokemon.pokeImageURL)
                        //.fetch()
                        Log.d(TAG, "Загружен аватар покемона ${pokemon.pokeName}")
                    }
                } else Log.d(TAG, "В Избранном пусто")
            }*/
        }
        else { // соединения с Интернетом нет
            this.arguments = bundleOf("isConnected" to false)
            //R.id.introFragment.internetOffTV.visibility = TextView.VISIBLE
            /*Toast.makeText(
                requireActivity().applicationContext,
                "Соединение с Интернетом отсутствует. Выйдите из приложения и подключите Интернет.",
                Toast.LENGTH_LONG
            ).show()*/
            Log.d(TAG, "Отсутствует соединение с Интернетом")
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
        else {
            if (!requireArguments().getBoolean("isConnected")) {
                introLayout.startProgressBar.visibility = ProgressBar.GONE
                introLayout.internetOffTV.visibility = TextView.VISIBLE
            }
        }
        return introLayout
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