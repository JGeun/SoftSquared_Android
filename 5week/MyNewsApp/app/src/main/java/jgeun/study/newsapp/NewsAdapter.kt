package jgeun.study.newsapp

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.facebook.drawee.view.SimpleDraweeView
import kotlinx.android.synthetic.main.activity_splash.view.*
import kotlinx.android.synthetic.main.row_news.view.*
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

class NewsAdapter(private val context: Activity, private val newsList: List<NewsItem>) : RecyclerView.Adapter<NewsAdapter.NewsAdapterHolder>(){
    private val REQUEST_MODIFY: Int = 10;
    class NewsAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image: SimpleDraweeView = itemView.news_image
        val cb_delete: CheckBox = itemView.cb_delete
        val title: TextView = itemView.news_title
        val content: TextView = itemView.news_content
        val btn_write: Button = itemView.btn_write
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapterHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_news, parent, false)
        return NewsAdapterHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsAdapterHolder, position: Int) {
        val currentItem = newsList[position]

        holder.image.setImageURI(currentItem.imageResource)
        holder.cb_delete.isChecked = false
        holder.cb_delete.setOnClickListener{
            Log.d("NewsAdapter", "clcick");
        }
        holder.title.setText(currentItem.title)
        holder.content.setText(currentItem.content)
        holder.btn_write.setOnClickListener {
            val activity = holder.itemView.context as Activity
            val intent = Intent(holder.itemView.context, ModifyActivity::class.java)
            Log.d("MainActivityCheck", "position: " + position)
            intent.putExtra("index", position)
            activity.startActivityForResult(intent, REQUEST_MODIFY)
        }
    }

    override fun getItemCount() = newsList.size

}