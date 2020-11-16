package jgeun.study.newsapp

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
import com.android.volley.RequestQueue
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.io.IOException
import java.net.URI
import java.net.URL

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var auth: FirebaseAuth
    private lateinit var myDataset : Array<String>
    private lateinit var queue : RequestQueue;
    private val list = ArrayList<NewsItem>()

    private lateinit var layoutParam : ViewGroup.LayoutParams
    private var mediaPlayer : MediaPlayer? = null
    private var pausePosition : Int = 0

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
        rv_newsPanel.adapter = NewsAdapter(newsList)
        rv_newsPanel.layoutManager = LinearLayoutManager(this)
        rv_newsPanel.setHasFixedSize(true)


        btn_profile.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")

        mediaPlayer = MediaPlayer.create(this, R.raw.music)
        if(pausePosition != 0) {
            Log.d("pausePositionCheck", Integer.toString(pausePosition))
            mediaPlayer?.seekTo(pausePosition)
        }
        mediaPlayer?.start()

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

    override fun onResume() {
        super.onResume()

    }

    override fun onPause() {
        super.onPause()
        pausePosition = mediaPlayer?.currentPosition!!
        mediaPlayer?.release();
    }

    override fun onDestroy() {
        super.onDestroy()
        if(mediaPlayer != null) {
            mediaPlayer?.stop()
            mediaPlayer?.release();
            mediaPlayer = null
        }

    }
}
