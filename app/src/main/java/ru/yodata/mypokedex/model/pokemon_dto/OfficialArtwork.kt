package ru.yodata.mypokedex.model.pokemon_dto


import com.google.gson.annotations.SerializedName

data class OfficialArtwork(
    @SerializedName("front_default")
    val frontDefault: String
)