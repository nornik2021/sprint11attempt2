package com.example.sprint11attempt2

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiURL {

    private const val  iTunesBaseUrl = "https://itunes.apple.com" // переменная с указанием URL
    private val retrofit = Retrofit.Builder()// подключаем библиотеку ретрофит
        .baseUrl(iTunesBaseUrl) // передаем базовый URL всех запросов через метод baseUrl
        .addConverterFactory(GsonConverterFactory.create()) // указываем конвертер
        .build()

     val itunesService = retrofit.create(ITunesSearchApi::class.java) // инициализируем сервис
}