package com.example.giveu.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.giveu.ArticleDetailActivity
import com.example.giveu.R
import com.example.giveu.model.Article

class ArticleAdapter(private var articleList: List<Article>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.articleTitle)
        val image: ImageView = view.findViewById(R.id.articleImage)
        val description: TextView = view.findViewById(R.id.articleDescription)
        val phone: TextView = view.findViewById(R.id.articlePhone)
        val location: TextView = view.findViewById(R.id.articleLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = articleList[position]

        holder.title.text = article.title
        holder.description.text = article.description
        holder.phone.text = "Telefono: ${article.phoneNumber ?: "N/A"}"
        holder.location.text = "Luogo: ${article.location ?: "N/A"}"

        Glide.with(holder.itemView.context)
            .load(article.imageUrl)
            .into(holder.image)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ArticleDetailActivity::class.java).apply {
                putExtra("title", article.title)
                putExtra("description", article.description)
                putExtra("imageUrl", article.imageUrl)
                putExtra("phone", article.phoneNumber)
                putExtra("location", article.location)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = articleList.size

    fun updateData(newList: List<Article>) {
        articleList = newList
        notifyDataSetChanged()
    }
    fun currentList(): List<Article> = articleList
}
