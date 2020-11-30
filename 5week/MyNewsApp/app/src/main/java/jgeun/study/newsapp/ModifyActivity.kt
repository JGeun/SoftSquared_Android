package jgeun.study.newsapp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_modify.*

class ModifyActivity : AppCompatActivity() {
    private val REQUEST_MODIFY: Int = 10;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)

        btn_modify.setOnClickListener {
            val getContentIntent = intent
            val index = getContentIntent.getIntExtra("index", 1)
            Log.d("MainActivityCheck", "modify: " + index.toString())

            getContentIntent.putExtra("index", index)
            getContentIntent.putExtra("title", et_mTitle.text.toString())
            getContentIntent.putExtra("contents", et_mContents.text.toString())

            setResult(Activity.RESULT_OK, getContentIntent)
            finish()
            Log.d("MainActivityCheck", "Modify: " + et_mTitle.text.toString() + " / " + et_mContents.text.toString())
        }
    }
}
