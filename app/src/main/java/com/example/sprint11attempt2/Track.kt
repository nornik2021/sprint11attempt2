package com.example.sprint11attempt2

import com.google.gson.annotations.SerializedName

data class Track( // создаем обьект для плучения данных от JSON
    val trackId: String,
    val trackName: String,
    val artistName: String,
    @SerializedName("trackTimeMillis") val trackTime: Long, // аннотация для указания какие ключи нужно искать
    val artworkUrl100: String
)
