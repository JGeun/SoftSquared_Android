package jgeun.study.listviewstudy

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.getTag
import androidx.core.content.ContextCompat.startActivity
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_modify.view.*
import kotlinx.android.synthetic.main.listview_item.view.*;
import java.io.Serializable

class CustomAdapter(context: Context, private val businessCardArrayList: ArrayList<BusinessCard>):
    BaseAdapter(){

    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int = businessCardArrayList.size

    override fun getItem(position: Int): Any = businessCardArrayList[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View{
        var view = inflater.inflate(R.layout.listview_item, parent, false)
        view.name_listview_item.text = businessCardArrayList[position].name
        view.contents_listview_item.text = businessCardArrayList[position].contents
        view.deleteSwitch.isChecked = businessCardArrayList[position].isSwitchChecked

        view.deleteSwitch.setOnCheckedChangeListener{ buttonView, isChecked->
            val goMain:Intent = Intent(view.context, MainActivity::class.java)
            if(view.deleteSwitch.isChecked) {
                goMain.putExtra("check", 1)
                businessCardArrayList[position].isSwitchChecked = true
            }
            else{
                goMain.putExtra("check", 0)
                businessCardArrayList[position].isSwitchChecked= false
            }
            goMain.putExtra("arrayList", businessCardArrayList)
            view.context.startActivity(goMain)
        }
        view.write.setOnClickListener {
            val goModifiy: Intent = Intent(view.context, ModifyActivity::class.java)
            goModifiy.putExtra("item", businessCardArrayList as Serializable)
            goModifiy.putExtra("itemPosition" , position)
            view.context.startActivity(goModifiy)
        }
        return view
    }
}