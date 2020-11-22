package jgeun.study.listviewstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_add_item.*
import java.io.Serializable

class AddItemActivity : AppCompatActivity() {
    private lateinit var  businessCardArrayList :ArrayList<BusinessCard>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        val intentItem = intent.getSerializableExtra("requireCreateItem")
        if(intentItem != null) {
            @Suppress("UNCHECKED_CAST")
            businessCardArrayList = intentItem as ArrayList<BusinessCard>
            Log.d("AddItemActivityCheck", businessCardArrayList.size.toString())
        }

        btn_create.setOnClickListener {
            val name = et_Name.text.toString()
            val contents = et_Contents.text.toString()
            businessCardArrayList.add(BusinessCard(name, contents, false))

            val goAddItem = Intent(this, MainActivity::class.java)
            goAddItem.putExtra("arrayList", businessCardArrayList)
            startActivity(goAddItem)
            finish()
        }
    }
}
