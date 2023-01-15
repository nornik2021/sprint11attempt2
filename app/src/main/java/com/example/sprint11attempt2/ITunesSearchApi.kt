package com.example.sprint11attempt2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesSearchApi { // создаем интерфейс

    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<TracksResponse> // аннотация Query позволяет фильтровать запрос со значениями параметра.
    // внутри нее мы указываем ключь параметра для фильтрации
    // Call - объект отвечающий на наш запрос

}