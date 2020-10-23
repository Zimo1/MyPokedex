/*=================================================================================================
     Суслопаров А.В.
     Ижевск
---------------------------------------------------------------------------------------------------
   Необходимо сделать приложение для работы с API https://pokeapi.co/docs/v2.html/#pokemon-section .
Приложение должно состоять из:
- заставка
- меню, содержит две кнопки с переходами на экраны 3 и 4
- экран поиска покемона по имени, содержит поле ввода имени покемона и кнопку поиск. Найденного покемона можно добавить в "избранные"
- экран вывода случайного покемона, по нажатию на кнопку должна отображаться информация о случайном покемоне. Найденного покемона можно добавить в "избранные"
- экран "избранных" покемонов - список покемонов, добавленных в "избранные"

Технологический стэк:
Kotlin
MVVM
Retrofit
Navigation Architecture Component
БД - Room (еще лучше, если сможете хранить их удаленно в Firebase)
============================================================================================
Примечание к решению:
На https://pokeapi.co нет команды поиска покемона по имени, есть только команда получения списка
имен всех покемонов со ссылками на инфу по каждому.
Поэтому в начале работы имена всех покемонов заносятся в список pokemonList, а потом по выбранным
покемонам инфа читается по отдельности, используя связанные с именами ссылки.
*/

package ru.yodata.mypokedex.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.yodata.mypokedex.R
import ru.yodata.mypokedex.model.pokemon_list_dto.Results
import ru.yodata.mypokedex.model.room.database.PokeDataBase

const val TAG = "MY_POKEDEX"
const val SERVER_URL = "https://pokeapi.co/api/v2/" //Сервер REST API, с которого запрашиваются данные
const val SERVER_RESPONSE_OK = 200 //HTTP-ответ сервера "Успешный запрос"
const val AVATAR_WIDTH = 384
const val AVATAR_HEIGTH = 384

//Глобальный список покемонов, заполняется один раз при старте приложения данными с сервера REST API.
// В нем хранится имя покемона и ссылка на REST API, по которой можно получить
// все остальные данные покемона
var pokemonList: List<Results>? = null
// База данных SQLite (ROOM)
var pokeDB: PokeDataBase? = null
const val ROOM_DB_FILE_NAME = "mypokedex.db"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}