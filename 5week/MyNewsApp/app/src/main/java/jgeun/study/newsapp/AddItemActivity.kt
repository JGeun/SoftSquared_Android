package jgeun.study.newsapp

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_add_item.*
import kotlinx.android.synthetic.main.activity_modify.*

class AddItemActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        btn_add.setOnClickListener {
            val setNewsIntent = intent

            setNewsIntent.putExtra("title", et_newTitle.text.toString())
            setNewsIntent.putExtra("contents", et_newContents.text.toString())

            setResult(Activity.RESULT_OK, setNewsIntent)
            finish()
        }
    }
}
