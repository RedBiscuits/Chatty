package com.example.myapplication.screens.chat

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.datastructures.chatty.R
import com.example.myapplication.adapters.CustomAdapter
import com.example.myapplication.models.DataModel

class AddGroupActivity : AppCompatActivity() {

    // Declaring the DataModel Array
    private var dataModel: ArrayList<DataModel>? = ArrayList()
    private var finalNumbers: ArrayList<String> = ArrayList()

    // Declaring the elements from the main layout file
    private lateinit var listView: ListView
    private lateinit var adapter: CustomAdapter
    private lateinit var groupName:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)
        val phones = intent.extras!!.getStringArrayList("phones")
        // Setting the adapter
        Log.d("data model" , phones.toString())


        for (i in phones!!){
            dataModel?.add(DataModel(i , false))
        }
        // Initializing the model and adding data
        listView = findViewById<View>(R.id.list_view_1) as ListView
        groupName = findViewById<View>(R.id.group_name) as TextView

        Log.d("data model" , dataModel.toString())

        adapter = CustomAdapter(dataModel!!, applicationContext)
        listView.adapter = adapter

        // Upon item click, checkbox will be set to checked
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val dataModel: DataModel = dataModel!![position]
            dataModel.checked = !dataModel.checked
            if(dataModel.checked){
                finalNumbers.add(dataModel.name!!)
            }else{
                finalNumbers.remove(dataModel.name)
            }
            adapter.notifyDataSetChanged()
        }
    }


}