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

    // Restituisce articoli locali + API combinati per categorie specificate
    fun getCombinedArticles(vararg categories: String): LiveData<List<Article>> {
        val result = MediatorLiveData<List<Article>>()

        val mappedCategories = categories.map {
            when (it.lowercase()) {
                "libri" -> "libri"
                "elettronica", "computer" -> "computer"
                "vestiti" -> "vestiti"
                "casa" -> "casa"
                "makeup", "trucco" -> "trucco"
                "animali", "accessori animali" -> "animali"
                "sport", "accessori sport" -> "sport"
                "auto", "accessori auto" -> "auto"
                else -> it.lowercase()
            }
        }

        val dbArticles = articleDao.getArticlesByCategories(mappedCategories)
        val apiArticles = MutableLiveData<List<Article>>()

        // Combina i dati da DB + API
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
}
