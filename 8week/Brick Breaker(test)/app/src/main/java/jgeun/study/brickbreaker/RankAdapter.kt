package jgeun.study.brickbreaker

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jgeun.study.brickbreaker.databinding.ActivityRankBinding

class RankAdapter(private val context: Activity, private val myDataset: ArrayList<UserData>) : RecyclerView.Adapter<RankAdapter.MyViewHolder>(){
    class MyViewHolder(val itemView: View) : RecyclerView.ViewHolder(itemView){
        val text = itemView.findViewById<TextView>(R.id.tv_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankAdapter.MyViewHolder {
        val inflaterView = LayoutInflater.from(parent.context).inflate(R.layout.list_data, parent, false)
        return RankAdapter.MyViewHolder(inflaterView)
    }

    override fun onBindViewHolder(holder: RankAdapter.MyViewHolder, position: Int) {
        holder.text.setText(myDataset[position].userName)
    }

    override fun getItemCount(): Int = myDataset.size

}