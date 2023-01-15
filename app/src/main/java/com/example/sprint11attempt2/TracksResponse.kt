package com.example.sprint11attempt2

import com.google.gson.annotations.SerializedName

class TracksResponse(@SerializedName("results") val searchResults: ArrayList<Track>) // создаем класс для параметров запроса