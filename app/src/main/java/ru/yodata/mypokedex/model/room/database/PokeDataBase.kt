package ru.yodata.mypokedex.model.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.yodata.mypokedex.model.room.dao.PokeDAO
import ru.yodata.mypokedex.model.room.entity.PokeCard

@Database(entities = arrayOf(PokeCard::class), version = 1)
abstract class PokeDataBase : RoomDatabase() {
    abstract fun pokeDao(): PokeDAO

    companion object {
        @Volatile private var instance: PokeDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){
            instance ?: buildDatabase(context).also { instance = it}
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(context,
            PokeDataBase::class.java, "todo-list.db")
            .build()
    }
}
