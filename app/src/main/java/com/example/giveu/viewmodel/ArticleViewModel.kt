package com.example.giveu.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.giveu.data.AppDatabase
import com.example.giveu.model.Article
import com.example.giveu.repository.ArticleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ArticleViewModel(application: Application) : AndroidViewModel(application) {

    private val articleDao = AppDatabase.getDatabase(application).articleDao()
    private val repository = ArticleRepository()

    // Inserisce un articolo nel DB locale
    fun insertArticle(article: Article) {
        viewModelScope.launch(Dispatchers.IO) {
            articleDao.insert(article)
        }
    }

    // Restituisce solo articoli locali filtrati per categoria
    fun getLocalArticlesByCategories(categories: List<String>): LiveData<List<Article>> {
        return articleDao.getArticlesByCategories(categories)
    }

    // API 1: DummyJson + DB locale combinati
    fun getCombinedArticles(vararg categories: String): LiveData<List<Article>> {
        val result = MediatorLiveData<List<Article>>()
        val mappedCategories = categories.map { it.lowercase() }
        val dbArticles = articleDao.getArticlesByCategories(mappedCategories)
        val apiArticles = MutableLiveData<List<Article>>()

        result.addSource(dbArticles) { dbList ->
            val apiList = apiArticles.value ?: emptyList()
            result.value = dbList + apiList
        }
        result.addSource(apiArticles) { apiList ->
            val dbList = dbArticles.value ?: emptyList()
            result.value = dbList + apiList
        }

        viewModelScope.launch(Dispatchers.IO) {
            val apiResults = mutableListOf<Article>()
            for (cat in mappedCategories) {
                try {
                    val fetched = repository.fetchArticles(cat)
                    apiResults.addAll(fetched)
                } catch (e: Exception) {
                    Log.e("ArticleViewModel", "Errore fetch API: $e")
                }
            }
            apiArticles.postValue(apiResults)
        }

        return result
    }

    // API 2: OpenLibrary per i libri
    fun fetchBooksFromOpenLibrary(): LiveData<List<Article>> {
        val result = MutableLiveData<List<Article>>()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val books = repository.fetchBooks("fiction")
                // ← fix: loop normale invece di forEach
                for (book in books) {
                    articleDao.insert(book)
                }
                result.postValue(books)
            } catch (e: Exception) {
                Log.e("ArticleViewModel", "Errore OpenLibrary: $e")
                result.postValue(emptyList())
            }
        }
        return result
    }
}