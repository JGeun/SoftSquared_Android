package jgeun.study.networttest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import jgeun.study.networkapiairport.R

class AirportInfoAdapter(private val myDataset: ArrayList<AirportInfo>) : RecyclerView.Adapter<AirportInfoAdapter.InfoViewHolder>() {
    class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val time = itemView.findViewById<TextView>(R.id.info_time)
        val flightId = itemView.findViewById<TextView>(R.id.info_flightId)
        val airline = itemView.findViewById<TextView>(R.id.info_airline)
        val terminalId = itemView.findViewById<TextView>(R.id.info_terminalId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.airport_info, parent, false)
        return InfoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.time.setText(myDataset[position].time)
        holder.flightId.setText(myDataset[position].flightId)
        holder.airline.setText(myDataset[position].airline)
        holder.terminalId.setText(myDataset[position].terminalId)
    }

    override fun getItemCount(): Int = myDataset.size
}