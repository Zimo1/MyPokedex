package ru.yodata.mypokedex.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ru.yodata.mypokedex.model.room.entity.PokeCard

@Dao
interface PokeDAO {
    // Create
    @Insert
    fun pokeInsert(pokeCard: PokeCard)

    // Read
    // Получение всех записей БД
    @Query("SELECT * FROM pokeCard ORDER BY rowid DESC") //новые записи будут идти первыми
    fun pokeGetAll(): LiveData<List<PokeCard>>

    // Получение одной записи БД по заданному имени покемона
    @Query("SELECT * FROM pokeCard WHERE pokeName == :pokeName")
    fun pokeGetByName(pokeName: String): PokeCard

    // Update
    @Update
    fun pokeUpdate(pokeCard: PokeCard)

    // Delete
    @Delete
    fun pokeDelete(pokeCard: PokeCard)

    @Query("DELETE from pokeCard")
    fun pokeDeleteAll()

}