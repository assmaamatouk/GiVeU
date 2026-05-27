package com.example.giveu.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.giveu.model.Article

@Dao
interface ArticleDao {

    // Inserisce o sostituisce un articolo
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: Article)

    // Restituisce articoli filtrati per più categorie
    @Query("SELECT * FROM articles WHERE LOWER(category) IN (:categories)")
    fun getArticlesByCategories(categories: List<String>): LiveData<List<Article>>
}
