package jgeun.study.listviewstudy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.listview_item.view.*
import java.io.Serializable
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(){

    private val TAG:String = "MainActivityCheck"

    private var businessCardArrayList = ArrayList<BusinessCard>()
    lateinit var customAdapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val getItemIntent = intent.getSerializableExtra("arrayList")
        if(getItemIntent != null){
            Log.d(TAG, "잘 들어왔습니다")
            @Suppress("UNCHECKED_CAST")
            businessCardArrayList  = getItemIntent as ArrayList<BusinessCard>
            if(isSwitchChecked()){
                btn_addItem.setImageDrawable(resources.getDrawable(R.drawable.ic_delete))
                btn_addItem.setTag("delete")
            }else{
                btn_addItem.setImageDrawable(resources.getDrawable(R.drawable.ic_additem))
                btn_addItem.setTag("add")
            }
        }else {
            Log.d(TAG, "잘 없습니다")
            if(businessCardArrayList.size == 0){
                for (x in 0..30) {
                    businessCardArrayList.add(BusinessCard("록", "하이", false))
                    businessCardArrayList.add(BusinessCard("담", "하이", false))
                    businessCardArrayList.add(BusinessCard("록", "하이", false))
                    businessCardArrayList.add(BusinessCard("담", "하이", false))
                }
            }
        }
        Log.d(TAG, businessCardArrayList.size.toString())

        btn_addItem.setOnClickListener{
            if(btn_addItem.getTag().toString().equals("add")){
                val intentAddItem = Intent(this, AddItemActivity::class.java)
                intentAddItem.putExtra("requireCreateItem", businessCardArrayList)
                startActivity(intentAddItem)
            }else{
                val size = businessCardArrayList.size -1
                var count: Int = 0
                Log.d(TAG, size.toString())
                for(i in 0..size)
                    if(businessCardArrayList.get(i).isSwitchChecked)
                        count += 1
                Log.d(TAG, "count: $count")

                for(i in size downTo 0 ){
                    if(businessCardArrayList.get(i).isSwitchChecked)
                        businessCardArrayList.removeAt(i)
                }
                Log.d(TAG, businessCardArrayList.size.toString())

                val updateMain = Intent(this, MainActivity::class.java)
                updateMain.putExtra("arrayList", businessCardArrayList)
                updateMain.putExtra("check", 0)
                startActivity(updateMain)
                finish()
            }
        }

        customAdapter = CustomAdapter(applicationContext, businessCardArrayList)
        listview_main.adapter = customAdapter
    }

    private fun isSwitchChecked() : Boolean{
        var count = 0
        for(i in 0 until businessCardArrayList.size)
            if(businessCardArrayList[i].isSwitchChecked){
                return true;
            }
        return false;
    }
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "start")
    }
}
