package ru.yodata.mypokedex.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_poke_search.*
import kotlinx.android.synthetic.main.fragment_poke_search.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.yodata.mypokedex.R
import ru.yodata.mypokedex.model.room.entity.PokeCard
import ru.yodata.mypokedex.model.REST_API_Client
import ru.yodata.mypokedex.model.pokemon_dto.PokemonData
import ru.yodata.mypokedex.model.pokemon_list_dto.Results

// Фрагмент поиска покемонов - как при вводе имени покемона с клавиатуры, так и случайным образом
class PokeSearchFragment : Fragment() {
    private var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val searchLayout = inflater.inflate(R.layout.fragment_poke_search, container, false)
        with (searchLayout) {
            // При запуске этого фрагмента ему на вход через bundle передается параметр -
            // "Режим поиска": ручной ("MANUAL") либо случайный ("RANDOM")
            // Вид фрагмента настраивается в зависимости от режима
            val searchMode = arguments!!.getString("searchMode")
            when (searchMode) {
                "MANUAL" -> {
                    searchHeaderTV.text = "Поиск покемона по имени"
                    searchNameTextView.visibility = TextView.VISIBLE
                }
                "RANDOM" -> {
                    searchHeaderTV.text = "Случайный поиск покемона"
                    searchNameTextView.visibility = TextView.INVISIBLE
                }
            }
            searchProgressBar.visibility = ProgressBar.INVISIBLE
            if (!pokemonList.isNullOrEmpty()) {
                val spinnerAdapter: ArrayAdapter<Results> = ArrayAdapter<Results>(
                    context, R.layout.support_simple_spinner_dropdown_item,
                    pokemonList!!
                )
                searchNameTextView.setAdapter(spinnerAdapter)
            }

            startSearchBtn.setOnClickListener{ onSearchButtonClick(this) }
        }
        return searchLayout
    }

    override fun onStart() {
        super.onStart()
        showPokemonCard()
    }

    // Получение данных покемона с сервера по ссылке и вывод их на экран
    fun getPokeDataByURLAndShow(pokeURL: String) {
        Log.d(TAG, "Проверка pokeURL...")
        if (!pokeURL.isNullOrEmpty()) {
            val shortPokeURL = pokeURL.substring(SERVER_URL.length)
            Log.d(TAG, "pokeURL = $shortPokeURL")
            disposable = REST_API_Client.getPokemonData(shortPokeURL)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,
                    {error -> Log.d(TAG, "Ошибка REST при запросе покемона: " + error.localizedMessage)})
        }
    }

    // Обработчик RxJava-отклика на запрос данных покемона с сервера REST API.
    // Запись полученных данных в карточку текущего покемона
    private fun handleResponse(response: PokemonData) {
        searchProgressBar.visibility = ProgressBar.VISIBLE
        pokeCard = with (response) {
            PokeCard(
                pokeName = name,
                pokeId = id,
                pokeImageURL = sprites.frontDefault,
                pokeAnimationURL = sprites.versions.generationV.blackWhite.animated.frontDefault,
                pokeWeight = weight,
                pokeHeight = height,
                pokeExperience = baseExperience
            )
        }
        searchProgressBar.visibility = ProgressBar.INVISIBLE
        showPokemonCard()
        Log.d(TAG, "Получен ответ REST по запрошенному покемону ${response.name}")
    }

    // Вывод на экран данных текущего покемона (карточка), включая аватар
    fun showPokemonCard() {
        if (Companion.pokeCard != null) {
            // Отобразить данные покемона из pokeCard на экране
            with (Companion.pokeCard!!) {
                pokeNameTV.text = pokeName.capitalize()
                pokeIdTV.text = "#" + pokeId.toString()
                pokeExpTV.text = pokeExperience.toString()
                pokeHeightTV.text = pokeHeight.toString()
                pokeWeightTV.text = pokeWeight.toString()
                // Отображение аватара покемона, загружаемого по ссылке
                Picasso.get()
                    .load("${(pokeAnimationURL ?: pokeImageURL) ?: R.drawable.pokesiluet96}" )
                    //.placeholder(R.drawable.pokesiluet96)
                    .error(R.drawable.pokesiluet96)
                    .resize(AVATAR_WIDTH, AVATAR_HEIGTH)
                    .into(pokeAvatarIV)
            }
            favoritesBtn.setOnClickListener {onFavoritesButtonClick()}
            // Сделать карточку покемона видимой
            pokeViewLayout.visibility = ConstraintLayout.VISIBLE
        }
    }

    //Обработчик нажатия на кнопку поиска покемона по введенному пользователем имени
    fun onSearchButtonClick(view: View) {
        //Если запущен случайный режим поиска покемонов, то строке поиска присвоить случайное имя
        // из существующего списка покемонов pokemonList
        if (arguments!!.getString("searchMode") == "RANDOM") {
            searchNameTextView.setText(pokemonList!!.random().pokeName)
        }
        if (searchNameTextView.text.isNullOrEmpty()) { //Строка ввода имени покемона пуста
            Toast.makeText(context,
                "Введите имя покемона в строке поиска!",
                Toast.LENGTH_LONG).show()
        }
        else { // В строке ввода имени покемона что-то есть...
            val pokeSearchName = searchNameTextView.text.toString().trim().toLowerCase()
            //Поиск введенного имени в списке покемонов
            val pokeItem = pokemonList!!.firstOrNull()
                { it.pokeName.trim().toLowerCase() == pokeSearchName }
            if (pokeItem != null) {
                // Имя покемона найдено в списке покемонов.
                // Прочитать его данные с сервера по URL, взятому из списка,  и отобразить их на экране
                //searchProgressBar.visibility = ProgressBar.VISIBLE
                pokeViewLayout.visibility = ConstraintLayout.INVISIBLE
                getPokeDataByURLAndShow(pokeItem.pokeURL)
                //searchProgressBar.visibility = ProgressBar.GONE
            }
            else { // Введено несуществующее имя покемона
                Toast.makeText(context,
                    "Покемона с именем '$pokeSearchName' не существует!",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    // Обработчик нажатия на кнопку занесения текущего покемона в Избранное
    fun onFavoritesButtonClick() {
        GlobalScope.launch {
            val pokeName = pokeNameTV.text.toString().toLowerCase()
            // Проверить содержится ли указанный покемон в Избранном (в БД)
            if (pokeDB!!.pokeDao().pokeGetByName(pokeName) == null) {
                // если нет - занести его в БД и перейти на экран "Избранное"
                pokeDB!!.pokeDao().pokeInsert(pokeCard!!)
                Log.d(TAG, "Покемон $pokeName внесен в Избранное")
                this@PokeSearchFragment.findNavController().
                    navigate(R.id.action_pokeSearchFragment_to_pokedexFragment)
            }
            else {
                // если такой покемон уже есть в Избранном, вывести сообщение об этом на экран
                Log.d(TAG, "Попытка внести в Избранное существующего там покемона $pokeName")
                activity!!.runOnUiThread(Runnable {
                Toast.makeText(
                    activity!!.applicationContext,
                    "Покемон с именем '$pokeName' уже находится в Избранном",
                    Toast.LENGTH_LONG
                ).show()
                } )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.dispose()
    }
    companion object {
        var pokeCard: PokeCard? = null // Карточка текущего покемона
    }

}