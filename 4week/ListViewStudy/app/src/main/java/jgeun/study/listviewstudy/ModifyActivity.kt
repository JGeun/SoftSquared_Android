package jgeun.study.listviewstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_modify.*
import kotlin.properties.Delegates

class ModifyActivity : AppCompatActivity() {
    private var businessCardArrayList = ArrayList<BusinessCard>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify)

        if(intent.hasExtra("item")) {
            @Suppress("UNCHECKED_CAST")
            businessCardArrayList = intent.getSerializableExtra("item") as ArrayList<BusinessCard>
        }
        val position:Int = intent.getIntExtra("itemPosition", 1)

        btn_modify.setOnClickListener {
            val mName = et_mName.text.toString()
            val mContent = et_mContents.text.toString()

            businessCardArrayList[position].name = mName
            businessCardArrayList[position].contents = mContent
            for(i in 0 until businessCardArrayList.size)
                businessCardArrayList[i].isSwitchChecked = false
            val goMain = Intent(this, MainActivity::class.java)
            goMain.putExtra("arrayList", businessCardArrayList)
            startActivity(goMain)
            finish()
        }
    }
}
