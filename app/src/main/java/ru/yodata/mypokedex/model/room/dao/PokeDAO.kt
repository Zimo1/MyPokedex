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
    // Получение всех записей БД в виде LiveData (для отображения в RecyclerView)
    @Query("SELECT * FROM pokeCard ORDER BY rowid DESC") //новые записи будут идти первыми
    fun pokeGetAll(): LiveData<List<PokeCard>>

    // Получение всех записей БД в обычном виде.
    // В предыдущей команде LiveData не отдает данные из БД напрямую (всегда возвращается null)
    @Query("SELECT * FROM pokeCard")
    fun pokeGetAllValue(): List<PokeCard>

    // Получение одной записи БД по заданному имени покемона
    @Query("SELECT * FROM pokeCard WHERE pokeName == :pokeName")
    fun pokeGetByName(pokeName: String): PokeCard

    // Update
    @Update
    fun pokeUpdate(pokeCard: PokeCard)

    // Delete
    @Delete
    fun pokeDelete(pokeCard: PokeCard)

    // Delete all
    @Query("DELETE from pokeCard")
    fun pokeDeleteAll()

}