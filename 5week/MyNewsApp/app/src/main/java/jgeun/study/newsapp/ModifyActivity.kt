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
            val intent = Intent()
            val index = intent.getIntExtra("index", 1)
            Log.d("MainActivityCheck", index.toString())

            intent.putExtra("index", index)
            intent.putExtra("title", et_mTitle.text.toString())
            intent.putExtra("contents", et_mContents.text.toString())

            setResult(Activity.RESULT_OK, intent)
            finish()
            Log.d("MainActivityCheck", "Modify: " + et_mTitle.text.toString() + " / " + et_mContents.text.toString())
        }
    }
}
