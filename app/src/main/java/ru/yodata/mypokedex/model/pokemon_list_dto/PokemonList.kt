package ru.yodata.mypokedex.model.pokemon_list_dto

import com.google.gson.annotations.SerializedName

   
data class PokemonList (

   @SerializedName("count") var count : Int,
   @SerializedName("next") var next : String,
   @SerializedName("previous") var previous : String,
   @SerializedName("results") var pokeList : List<Results>

)