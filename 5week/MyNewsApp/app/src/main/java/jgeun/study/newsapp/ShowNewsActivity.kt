package jgeun.study.newsapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.net.toUri
import kotlinx.android.synthetic.main.activity_show_news.*

class ShowNewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_news)

        val getIntent = intent

        val newsPhotoUri = getIntent.getStringExtra("newsPhoto")
        if(newsPhotoUri != null){
            iv_newsPhoto.setImageURI(Uri.parse(newsPhotoUri))
        }else {
            iv_newsPhoto.setImageURI(Uri.parse("android.resource://jgeun.study.newsapp/"+R.drawable.pencil));
        }
        tv_newsTitle.setText(getIntent.getStringExtra("newsTitle"))
        tv_newsContents.setText(getIntent.getStringExtra("newsContent"))

        btn_back.setOnClickListener{
            finish()
        }
    }
}
