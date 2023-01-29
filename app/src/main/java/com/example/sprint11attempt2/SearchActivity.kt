package com.example.sprint11attempt2

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {


    private var inputSearchText = ""
    private val resultsTracksList = ArrayList<Track>()
    private lateinit var resultsTrackAdapter: TrackAdapter
    private lateinit var historyTrackAdapter: TrackAdapter
    private lateinit var sharedPrefsListener: SharedPreferences.OnSharedPreferenceChangeListener // добавили переменную сссылающуюся на шаред преференс
    private lateinit var enteringText: EditText
    private lateinit var errorPlaceholder: LinearLayout
    private lateinit var phMessage: TextView
    private lateinit var errorImage: ImageView
    private lateinit var clearButton: ImageView
    private lateinit var refreshButton: Button
    private lateinit var returnToMain: ImageView
    private lateinit var searchHistoryViewGroup: LinearLayout
    private lateinit var buttonClearHistory: Button

    private lateinit var recyclerHistoryTrackList: RecyclerView
    private lateinit var recyclerResultsTrackList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {  // функция которая задает начальную установку параметров
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search) // ссылается на activity_search


        val sharedPrefs = App.instance.sharedPrefs // ссылаемся на переменную с шаред преф
        val searchHistory = SearchHistory(sharedPrefs)
        resultsRecycler(searchHistory)
        historyRecycler(searchHistory, sharedPrefs)
        installView()

        returnToMain.setOnClickListener { // обработчик нажатия кнопки
            finish()
        }

        clearButton.setOnClickListener {// обработчик нажатия кнопки
            enteringText.setText("") // ложим пустой текст
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.hideSoftInputFromWindow(enteringText.windowToken, 0)
            resultsTracksList.clear() // очищаем треклист
            resultsTrackAdapter.notifyDataSetChanged() // метод адаптера который сообщает что нужно список
            // перерисовать согласно новым данным

        }

        refreshButton.setOnClickListener {// обработчик нажатия кнопки
            errorPlaceholder.visibility = View.GONE
            refreshButton.visibility = View.GONE
            iTunesSearch()
        }

        enteringText.setOnEditorActionListener { _, actionId, _ -> // прослушиватель, который будет вызываться при выполнении действия в текстовом
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (enteringText.text.isNotEmpty()) {
                    iTunesSearch()
                }
            }
            false
        }

        val searchTextWatcher = object : TextWatcher { // метод до того как ввели текст
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { //  метод вызывается до изменений
                errorPlaceholder.visibility = View.GONE
                refreshButton.visibility = View.GONE
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { // символы, начинающиеся с start , только что заменили старый текст длиной before
                clearButton.visibility = clearButtonVisibility(s)
                inputSearchText = s.toString()
                searchHistoryViewGroup.visibility =
                    if (
                        enteringText.hasFocus() &&
                        s?.isEmpty() == true &&
                        searchHistory.tracksHistory.isNotEmpty()
                    ) View.VISIBLE else View.GONE
            }

            override fun afterTextChanged(s: Editable?) { // этот есть
            }
        }
        enteringText.addTextChangedListener(searchTextWatcher) // добавили прослушиватель измененного текста

        enteringText.setOnFocusChangeListener { view, hasFocus -> //Определение интерфейса для обратного вызова, который будет вызываться при изменении состояния фокуса представления
            searchHistoryViewGroup.visibility =
                if (
                    hasFocus &&
                    enteringText.text.isEmpty() &&
                    searchHistory.tracksHistory.isNotEmpty()
                ) View.VISIBLE else View.GONE
        }

        buttonClearHistory.setOnClickListener {
            searchHistory.deleteItems()
            searchHistoryViewGroup.visibility = View.GONE
        }
    }

    private fun historyRecycler(
        searchHistory: SearchHistory,
        sharedPrefs: SharedPreferences
    ) {
        historyTrackAdapter = TrackAdapter(this, searchHistory)
        searchHistory.loadTracksFromJson()
        historyTrackAdapter.tracks = searchHistory.tracksHistory

        sharedPrefsListener = SharedPreferences.OnSharedPreferenceChangeListener{ _, key ->
            if (key == SEARCH_HISTORY_KEY) historyTrackAdapter.notifyDataSetChanged()
        }
        sharedPrefs.registerOnSharedPreferenceChangeListener(sharedPrefsListener)

        recyclerHistoryTrackList = findViewById(R.id.rvSearchHistory)
        recyclerHistoryTrackList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerHistoryTrackList.adapter = historyTrackAdapter
    }

    private fun resultsRecycler(searchHistory: SearchHistory) { // добавили рециклер вью для результата поиска
        resultsTrackAdapter = TrackAdapter( this,searchHistory)
        resultsTrackAdapter.tracks = resultsTracksList

        recyclerResultsTrackList = findViewById(R.id.rvResults)
        recyclerResultsTrackList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerResultsTrackList.adapter = resultsTrackAdapter
    }


    private fun installView() {

        returnToMain = findViewById(R.id.returnToMain) //  стрелка - возврат назад
        enteringText = findViewById(R.id.enteringText) // поле для ввода текта в поиск
        errorPlaceholder = findViewById(R.id.errorPlaceholder) // поле отображения ошибки поиска
        phMessage = findViewById(R.id.phMessage) // поле отображения текста ошибки поиска
        errorImage = findViewById(R.id.errorImage) // отображение картинки ошибки поиска
        refreshButton = findViewById(R.id.refreshButton) // отображение кнопки обновить
        clearButton = findViewById(R.id.clear) // отображение крестика для стирания текста
        searchHistoryViewGroup = findViewById(R.id.searchHistory) // поле история поиска
        buttonClearHistory = findViewById(R.id.buttonClearHistory)// кнопка обновить
    }


  companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val SEARCH_HISTORY_KEY = "key_for_search_history"

    }

    enum class NegativeResultMessage { //   отображение видимости картинки
        NOTHING_FOUND,
        ERROR_CONNECTION
    }


   private fun iTunesSearch() {
        ApiURL.itunesService
            .search(enteringText.text.toString())  // ищем текст
            .enqueue(object : Callback<TracksResponse> { // что бы вызвать запрос мы используем метод enqueue

                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse( // метод вызывается когда сервер дал ответ на наш запрос
                    call: Call<TracksResponse>, // очищаем треклист
                    response: Response<TracksResponse>
                ) {
                    if (response.code() == 200) { // если респонс код равен 200
                        resultsTracksList.clear()

                        if (response.body()?.searchResults?.isNotEmpty() == true) { // метод body() возвращает результат в виде объекта кот. указан в Call
                            // если запрос был удачным - получаем ответ в JSON тексте
                            resultsTracksList.addAll(response.body()?.searchResults!!) // добавляем все в треклист результаты поиска
                            resultsTrackAdapter.notifyDataSetChanged() // добавляем результаты поиска в трек адаптер
                        }
                        if (resultsTracksList.isEmpty()) { // если треклист пуст
                            negativeResultMessage(NegativeResultMessage.NOTHING_FOUND) // вызываем функцию и показываем картинку что ничего не найдено
                        }
                    } else {
                        negativeResultMessage(NegativeResultMessage.ERROR_CONNECTION) // в остальных случаях показываем картинку нет соединения
                    }
                }

                override fun onFailure(call: Call<TracksResponse>, t: Throwable) { // метод вызывается когда не смогли установить соединение с сервером
                    negativeResultMessage(NegativeResultMessage.ERROR_CONNECTION) // выводим ошибку
                }
            })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun negativeResultMessage(errorCode: NegativeResultMessage) {
        errorPlaceholder.visibility = View.VISIBLE
        resultsTracksList.clear()
        resultsTrackAdapter.notifyDataSetChanged()
        when (errorCode) {
            NegativeResultMessage.NOTHING_FOUND -> {
                phMessage.text = getString(R.string.nothing_found)
                Glide.with(errorImage)
                    .load(R.drawable.nothing_found)
                    .into(errorImage)
            }
            NegativeResultMessage.ERROR_CONNECTION -> {
                phMessage.text = getString(R.string.no_connection)
                Glide.with(errorImage)
                    .load(R.drawable.no_connection)
                    .into(errorImage)
                refreshButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) { // метод для сохранения промежуточного состояния активности
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, inputSearchText) // ключь и выдаваемое значение
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) { // метод для востановления данных которые переданы в onSaveInstanceState
        super.onRestoreInstanceState(savedInstanceState)
        inputSearchText = savedInstanceState.getString(SEARCH_TEXT, "")
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }
}
