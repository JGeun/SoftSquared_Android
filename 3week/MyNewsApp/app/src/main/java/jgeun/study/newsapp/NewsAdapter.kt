package jgeun.study.newsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.row_news.view.*
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

class NewsAdapter(private val newsList: List<NewsItem>) : RecyclerView.Adapter<NewsAdapter.NewsAdapterHolder>(){
    class NewsAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image: SimpleDraweeView = itemView.news_image
        val title: TextView = itemView.news_title
        val content: TextView = itemView.news_content
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapterHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_news, parent, false)
        return NewsAdapterHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsAdapterHolder, position: Int) {
        val currentItem = newsList[position]

        holder.image.setImageURI(currentItem.imageResource)
        holder.title.setText(currentItem.title)
        holder.content.setText(currentItem.content)
    }

    override fun getItemCount() = newsList.size

}