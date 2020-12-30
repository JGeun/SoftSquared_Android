package jgeun.study.networkapiairport

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jgeun.study.networkapiairport.databinding.ActivityArrivalBinding
import jgeun.study.networttest.AirportInfo
import jgeun.study.networttest.AirportInfoAdapter
import okhttp3.*
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.xml.sax.InputSource
import java.io.IOException
import java.lang.Exception
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class ArrivalActivity : AppCompatActivity() {
    private lateinit var tv_test: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var myDataset:ArrayList<AirportInfo> = ArrayList()
    private lateinit var arrival_tv_noData: TextView
    private var airportCode : String = ""
    private var urlPath = "http://openapi.airport.kr/openapi/service/StatusOfPassengerFlightsDS/getPassengerArrivalsDS?" +
            "ServiceKey=tt7pHVaMtOn9XV7%2BsAPlODP%2Bt8E4lc2%2FQNB8tSELA7FQ%2BeRtgv8WA1yddxgRZmx6YD6BSLOhrmPtbg4pFC7WGg%3D%3D&" +
            "airport_code="
    private lateinit var requestUrl : URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arrival)

        val binding = ActivityArrivalBinding.inflate(layoutInflater)
        arrival_tv_noData = binding.arrivalTvNoData

        val nationSpinner: Spinner = binding.arrivalSpNation
        ArrayAdapter.createFromResource(this, R.array.nations_array, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            nationSpinner.adapter = adapter
        }
        nationSpinner.onItemSelectedListener = SpinnerSelectedListener(airportCode)
        viewManager = LinearLayoutManager(this)
        viewAdapter = AirportInfoAdapter(myDataset)
        recyclerView = binding.arrivalRvAirportInfo.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }
        binding.arrivalBtnSearch.setOnClickListener{
            setAirportInfoArray()
        }
        setContentView(binding.root)
    }
    class SpinnerSelectedListener : AdapterView.OnItemSelectedListener{
        private var airportCode: String

        constructor(airportCode: String){
            this.airportCode = airportCode
        }
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            airportCode = parent?.getItemAtPosition(position).toString().substring(0, 3)
            (parent?.context as ArrivalActivity).airportCode =  parent.getItemAtPosition(position).toString().substring(0, 3)
            Log.d("airportCodeSpinner", airportCode + " / "  + (parent.context as ArrivalActivity).airportCode)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            Log.d("Depart", "선택되지 않았습니다")
        }
    }

    fun setAirportInfoArray(){
        myDataset.clear()
        var urlArrivalPath = urlPath
        urlArrivalPath += airportCode
        Log.d("airportSet:", airportCode)
        Log.d("urlDepartPath", urlArrivalPath)
        requestUrl = URL(urlArrivalPath)

        val okHttpClient = OkHttpClient()
        okHttpClient.newCall(
            Request.Builder()
            .url(requestUrl)
            .build()).enqueue(object: Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("OkHttp", e.message)
            }
            override fun onResponse(call: Call, response: Response) {
                Log.d("PASSED", "PASS")
                if(response.body != null){
                    GetXMLTask(requestUrl, myDataset, viewAdapter, arrival_tv_noData).execute()
                }
            }
        })
    }

    private class GetXMLTask(private var url: URL, private var myDataset: ArrayList<AirportInfo>, private var viewAdapter: RecyclerView.Adapter<*>, private var arrival_tv_noData: TextView)
        : AsyncTask<String, Void, Document>() {

        override fun doInBackground(vararg params: String?): Document? {
            var doc: Document? = null
            try{
                val dbf = DocumentBuilderFactory.newInstance()
                val db = dbf.newDocumentBuilder()
                doc = db.parse(InputSource(url.openStream()))
                doc.documentElement.normalize()
            }catch(e : Exception){
                Log.d("Error", e.message)
            }
            return doc
        }

        override fun onPostExecute(result: Document?) {
            Log.d("MyData", "execute")
            val itemNodeList: NodeList = result!!.getElementsByTagName("item")
            for(i in 0 until itemNodeList.length){
                val node: Node = itemNodeList.item(i)
                val element: Element = node as Element

                val estimatedDateTime = element.getElementsByTagName("estimatedDateTime").item(0).childNodes.item(0).nodeValue
                val flightId = element.getElementsByTagName("flightId").item(0).childNodes.item(0).nodeValue
                val airline = element.getElementsByTagName("airline").item(0).childNodes.item(0).nodeValue
                val terminalId = element.getElementsByTagName("terminalid").item(0).childNodes.item(0).nodeValue

                val time: String =  estimatedDateTime.substring(2,4) + "/" + estimatedDateTime.substring(4,6) +"/"+ estimatedDateTime.substring(6,8)+
                        " " + estimatedDateTime.substring(8,10) + ":" + estimatedDateTime.substring(10,12)
                myDataset.add(AirportInfo(time, flightId, airline, terminalId))
            }
            if(myDataset.size == 0)
                arrival_tv_noData.visibility = View.VISIBLE
            else
                arrival_tv_noData.visibility = View.INVISIBLE
            viewAdapter.notifyDataSetChanged()

            super.onPostExecute(result)
        }
    }
}