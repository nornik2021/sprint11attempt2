package com.example.sprint11attempt2

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity:AppCompatActivity() {

    private var userInputSearchText =""

    private val iTunesBaseUrl = "https://itunes.apple.com" // переменная с указанием URL

    private val retrofit = Retrofit.Builder() // подключаем библиотеку ретрофит

        .baseUrl(iTunesBaseUrl)// передаем базовый URL всех запросов через метод baseUrl
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesService = retrofit.create(ITunesSearchApi::class.java)// инициализируем сервис

    private val tracksList= ArrayList<Track>() // подключаем дата класс Track
    private val trackAdapter=TrackAdapter()// подключаем адаптер

    private lateinit var searchInput: EditText // переменная с отложенной инициализацией с указанием типа
    private lateinit var errorPlaceholder: LinearLayout // переменная с отложенной инициализацией с указанием типа
    private lateinit var placeholderMessage: TextView // переменная с отложенной инициализацией с указанием типа
    private lateinit var placeholderImage: ImageView // переменная с отложенной инициализацией с указанием типа
    private lateinit var clearButton: ImageView // переменная с отложенной инициализацией с указанием типа
    private lateinit var renewButton: Button // переменная с отложенной инициализацией с указанием типа
    private lateinit var arrowReturn: ImageView // переменная с отложенной инициализацией с указанием типа
    // переменные с отложенной инициализацией нужны для того что бы не проверять на ноль

    override fun onCreate(savedInstanceState: Bundle?) { // функция которая задает начальную установку параметров
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        initView()

        arrowReturn.setOnClickListener { // слушатель нажатия возврат на активити маин
         val returnIntent = Intent(this,MainActivity::class.java)
            startActivity(returnIntent)
        }

        clearButton.setOnClickListener {
            searchInput.setText("")
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(searchInput.windowToken,0)
            tracksList.clear()
            trackAdapter.notifyDataSetChanged()
        }
        renewButton.setOnClickListener {
            errorPlaceholder.visibility = View.GONE
            renewButton.visibility = View.GONE
            iTunesSearch()

        }
        searchInput.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (searchInput.text.isNotEmpty()) {
                    iTunesSearch()
                }
            }
            false
        }
        val searchTextWatcher= object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                errorPlaceholder.visibility = View.GONE
                renewButton.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                userInputSearchText = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        }
        searchInput.addTextChangedListener (searchTextWatcher)

        trackAdapter.tracks = tracksList

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view_items) // переменная ссылается на список, который будет выведен на экран
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = trackAdapter

    }

    private fun initView() { // метод начальный вид
        arrowReturn = findViewById(R.id.arrow_return) //  стрелка - возврат назад
        searchInput = findViewById(R.id.inputEditText) // поле для ввода текта в поиск
        errorPlaceholder = findViewById(R.id.errorPlaceholder) // отображение ошибки
        placeholderMessage = findViewById(R.id.placeholderMessage) // отображение текста
        placeholderImage = findViewById(R.id.placeholderErrorImage) // отображение картинки ошибки
        renewButton = findViewById(R.id.renewButton) // отображение кнопки обновить
        clearButton = findViewById(R.id.clearIcon) // отображение крестика для стирания текста
    }

    companion object{
        const val SEARCH_TEXT = "SEARCH_TEXT" // постоянная переменная для ключа
    }

    private fun  iTunesSearch() {
        itunesService
            .search(searchInput.text.toString())// ищем текст
            .enqueue(object : Callback<TracksResponse>{
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TracksResponse>,
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) {
                        tracksList.clear()
                    if (response.body()?.searchResults?.isNotEmpty() == true) {
                        tracksList.addAll(response.body()?.searchResults!!)
                        trackAdapter.notifyDataSetChanged()
                    }
                    if (tracksList.isEmpty()) {
                        negativeResultMessage(NegativeResultMessage.NOTHING_FOUND)
                    }
                } else {
                        negativeResultMessage(NegativeResultMessage.ERROR_CONNECTION)
                    }
                }

                /**
                 * Invoked when a network exception occurred talking to the server or when an unexpected exception
                 * occurred creating the request or processing the response.
                 */
                override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
                    negativeResultMessage(NegativeResultMessage.ERROR_CONNECTION)
                }
            })
    }

    enum class NegativeResultMessage {
        NOTHING_FOUND,
        ERROR_CONNECTION
    }

    private fun negativeResultMessage(errorCode: NegativeResultMessage) {
        errorPlaceholder.visibility = View.VISIBLE
        tracksList.clear()
        trackAdapter.notifyDataSetChanged()
        when (errorCode) { // когда код ошибки
            NegativeResultMessage.NOTHING_FOUND -> { //NOTHING_FOUND
                placeholderMessage.text = getString(R.string.nothing_found) // выводим сообщение и картинку
                Glide.with(placeholderImage)
                    .load(R.drawable.nothing_found)
                    .into(placeholderImage)
            }
            NegativeResultMessage.ERROR_CONNECTION -> {
                placeholderMessage.text = getString(R.string.no_connection)
                Glide.with(placeholderImage)
                    .load(R.drawable.no_connection)
                    .into(placeholderImage)
                renewButton.visibility = View.VISIBLE

            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {// метод для сохранения промежуточного состояния активности
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, userInputSearchText)// ключь и выдаваемое значение
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {// метод для востановления данных которые переданы в onSaveInstanceState
        super.onRestoreInstanceState(savedInstanceState)
        userInputSearchText = savedInstanceState.getString(SEARCH_TEXT,"")
    }
    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) { // возвратить если isNullOrEmpty - значки не видимы
            View.GONE
        }else {
            View.VISIBLE // в другом случае - видимы
        }
    }

}