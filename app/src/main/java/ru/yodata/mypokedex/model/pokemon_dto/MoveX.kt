package ru.yodata.mypokedex.model.pokemon_dto


import com.google.gson.annotations.SerializedName

data class MoveX(
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)