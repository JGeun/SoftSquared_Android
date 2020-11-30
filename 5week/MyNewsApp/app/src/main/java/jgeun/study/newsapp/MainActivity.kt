package jgeun.study.newsapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.TransitionDrawable
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.RequestQueue
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.auth.FirebaseAuth
import com.melnykov.fab.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.IOException
import java.net.URI
import java.net.URL
import java.util.*
import java.util.Arrays.sort
import java.util.Collections.sort
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivityCheck"
    private lateinit var auth: FirebaseAuth
    private lateinit var myDataset : Array<String>
    private lateinit var queue : RequestQueue;
    private val list = ArrayList<NewsItem>()
    private val removeRvIndexList = ArrayList<Int>()

    private lateinit var layoutParam : ViewGroup.LayoutParams
    private var mediaPlayer : MediaPlayer? = null
    private var pausePosition : Int = 0

    private val REQUEST_MODIFY: Int = 10
    private val REQUEST_ADDITEM: Int = 20
    private val REQUEST_DELETE: Int = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        musicTone.setOnClickListener {
            Log.d("MusicTone", "click")
            if(musicTone.getTag().equals("musicPlay")){
                musicTone.setTag("musicStop")
                musicTone.setImageDrawable(resources.getDrawable(R.drawable.musictone_stop))

                mediaPlayer?.pause()
            }else{
                musicTone.setTag("musicPlay")
                musicTone.setImageDrawable(resources.getDrawable(R.drawable.musictone))
                mediaPlayer?.start()

            }
        }

        Fresco.initialize(this)

        val jsonFileString = getJsonDataFromAsset(applicationContext, "news.json")
        Log.d("data", jsonFileString)

        val json = JSONObject(jsonFileString)
        val arrayArticles = json.getJSONArray("articles")
        for(i in 0 until arrayArticles.length()){
            val obj: JSONObject = arrayArticles.getJSONObject(i)
            Log.d("JSONOBJECT_Check", obj.getString(("title")))
            val item: NewsItem = NewsItem(
                Uri.parse(obj.getString("urlToImage")),
                obj.getString("title"),
                obj.getString("description"));
            list += item
        }

        val newsList = list
        rv_newsPanel.adapter = NewsAdapter(this, newsList, removeRvIndexList)
        rv_newsPanel.layoutManager = LinearLayoutManager(this)
        rv_newsPanel.setHasFixedSize(true)

        btn_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }



        btn_addItem.attachToRecyclerView(rv_newsPanel);
        btn_addItem.setOnClickListener{
            if(btn_addItem.getTag().toString().equals("add")){
                val intent = Intent(this, AddItemActivity::class.java)
                startActivityForResult(intent, REQUEST_ADDITEM)
            }else{
                val size = removeRvIndexList.size-1
                for(i in size downTo 0 step 1){
                    list.removeAt(removeRvIndexList.get(i))
                }
                removeRvIndexList.clear()
                btn_addItem.setTag("add")
                btn_addItem.setImageDrawable(resources.getDrawable(R.drawable.ic_additem))
                rv_newsPanel.adapter?.notifyDataSetChanged();
            }

        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")

        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        if(musicTone.getTag().equals("musicPlay")) {
            if (pausePosition != 0) {
                Log.d("pausePositionCheck", Integer.toString(pausePosition))
                mediaPlayer?.seekTo(pausePosition)
            }
            mediaPlayer?.start()
        }
    }

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    override fun onPause() {
        super.onPause()

        if(mediaPlayer != null) {
            mediaPlayer?.stop()
            pausePosition = mediaPlayer?.currentPosition!!
            mediaPlayer?.release();
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_MODIFY && resultCode == Activity.RESULT_OK) {
            val index: Int = data!!.getIntExtra("index", 1)
            Log.d(TAG, "기존의: " + list.get(index).title + " / "  + list.get(index).content)

            val title :String? = data!!.getStringExtra("title")
            val content :String? = data!!.getStringExtra("title")
            Log.d(TAG, "바꿀: " + index.toString()+ " " + title +" / " + content)
            list.get(index).title = title
            list.get(index).content = content

            rv_newsPanel.adapter?.notifyDataSetChanged();
            Log.d(TAG, "바뀐: " + list.get(index).title + " / "  + list.get(index).content)
        }else if(requestCode == REQUEST_ADDITEM && resultCode == Activity.RESULT_OK){
            val title :String? = data!!.getStringExtra("title")
            val content :String? = data!!.getStringExtra("title")
            val uri: Uri = Uri.parse("android.resource://jgeun.study.newsapp/drawable/splash_image.png");
            list.add(NewsItem(uri, title, content))
        }
    }
}
