package ru.yodata.mypokedex.model.pokemon_list_dto

import com.google.gson.annotations.SerializedName

   
data class Results (

   @SerializedName("name") var pokeName : String,
   @SerializedName("url") var pokeURL : String

)
{
   // Необходимо переопределить метод toString для корректной работы выпадающего списка в элементе
   // AutoCompleteTextView на экране поиска покемона по имени (PokeSearchFragment)- там нужно выводить
   // только имена покемонов
   override fun toString(): String {
      return this.pokeName
   }
}