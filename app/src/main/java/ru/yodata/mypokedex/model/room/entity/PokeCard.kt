package ru.yodata.mypokedex.model.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Выборка некоторых данных покемона, которые будут выводиться на экран и храниться в БД
@Entity
data class PokeCard(
    @PrimaryKey
    val pokeId: Int, // Идентификатор
    val pokeName: String, // Имя
    val pokeImageURL: String?, // Ссылка на статичный аватар
    val pokeAnimationURL: String?, //Ссылка на анимированный аватар (gif)
    val pokeWeight: Int, // Вес
    val pokeHeight: Int, // Рост
    val pokeExperience: Int // Базовый опыт
)