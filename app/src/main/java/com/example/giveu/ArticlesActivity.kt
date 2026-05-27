package com.example.giveu

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.giveu.adapter.ArticleAdapter
import com.example.giveu.viewmodel.ArticleViewModel

class ArticlesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArticleAdapter
    private val viewModel: ArticleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_articoli)

        setupRecyclerView()
        setupBackButton()

        val categories = intent.getStringArrayExtra("categories")

        if (categories.isNullOrEmpty()) {
            Toast.makeText(this, "Nessuna categoria specificata.", Toast.LENGTH_LONG).show()
            return
        }

        val normalizedCategories = categories.map { it.lowercase() }

        viewModel.getCombinedArticles(*normalizedCategories.toTypedArray()).observe(this) { articles ->
            if (articles.isEmpty()) {
                Toast.makeText(this, "Nessun articolo trovato per queste categorie.", Toast.LENGTH_SHORT).show()
            } else {
                adapter.updateData(articles)
            }
        }
    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ArticleAdapter(emptyList())
        recyclerView.adapter = adapter
    }

    private fun setupBackButton() {
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }
    }
}

