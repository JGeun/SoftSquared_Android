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
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_splash.view.*
import kotlinx.android.synthetic.main.row_news.view.*
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class NewsAdapter(private val context: Activity, private val newsList: List<NewsItem>, private val removeRvIndexList: ArrayList<Int>) : RecyclerView.Adapter<NewsAdapter.NewsAdapterHolder>(){
    private val REQUEST_MODIFY: Int = 10;
    private val REQUEST_DELETE: Int = 30;

    class NewsAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val image: SimpleDraweeView = itemView.news_image
        val cb_delete: CheckBox = itemView.cb_delete
        val title: TextView = itemView.news_title
        val content: TextView = itemView.news_content
        val btn_write: Button = itemView.btn_write
        val rootView: View = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapterHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_news, parent, false)
        return NewsAdapterHolder(itemView)
    }

    override fun onBindViewHolder(holder: NewsAdapterHolder, position: Int) {
        val activity = holder.itemView.context as Activity
        val currentItem = newsList[position]
        holder.itemView.isClickable
        holder.itemView.isEnabled
        holder.itemView.setOnClickListener{
            val goNewsIntent = Intent(context, ShowNewsActivity::class.java)
            goNewsIntent.putExtra("newsPhoto", currentItem.imageResource.toString())
            goNewsIntent.putExtra("newsTitle", currentItem.title)
            goNewsIntent.putExtra("newsContent", currentItem.content)
            activity.startActivity(goNewsIntent)
        }
        holder.image.setImageURI(currentItem.imageResource)
        holder.cb_delete.isChecked = false
        holder.cb_delete.setOnClickListener{
            Log.d("Position", "이 그림은: " + position.toString())
            if(holder.cb_delete.isChecked)
                removeRvIndexList.add(position)
            else
                removeRvIndexList.removeAt(removeRvIndexList.indexOf(position))
            removeRvIndexList.sort()
            if(removeRvIndexList.size != 0) {
                context.btn_addItem.setImageDrawable(context.resources.getDrawable(R.drawable.ic_delete))
                context.btn_addItem.setTag("delete")
            }
            else {
                context.btn_addItem.setImageDrawable(context.resources.getDrawable(R.drawable.ic_additem))
                context.btn_addItem.setTag("add")
            }
        }
        holder.title.setText(currentItem.title)
        holder.content.setText(currentItem.content)
        holder.btn_write.setOnClickListener{
            val intent = Intent(holder.itemView.context, ModifyActivity::class.java)
            Log.d("MainActivityCheck", "position: " + position)
            intent.putExtra("index", position)
            activity.startActivityForResult(intent, REQUEST_MODIFY)
        }
        holder.rootView.setTag(position)
    }

    override fun getItemCount() = newsList.size

}