package com.example.myapplication.screens.chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import com.datastructures.chatty.R
import com.example.myapplication.adapters.CustomAdapter
import com.example.myapplication.models.DataModel

class AddGroupActivity : AppCompatActivity() {

    // Declaring the DataModel Array
    private var dataModel: ArrayList<DataModel>? = null

    // Declaring the elements from the main layout file
    private lateinit var listView: ListView
    private lateinit var adapter: CustomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        // Initializing the elements from the main layout file
        listView = findViewById<View>(R.id.list_view_1) as ListView

        // Initializing the model and adding data
        // False = not checked; True = checked
        dataModel = ArrayList<DataModel>()
        dataModel!!.add(DataModel("Apple Pie", false))
        dataModel!!.add(DataModel("Banana Bread", false))
        dataModel!!.add(DataModel("Cupcake", false))
        dataModel!!.add(DataModel("Donut", true))
        dataModel!!.add(DataModel("Eclair", true))
        dataModel!!.add(DataModel("Froyo", true))
        dataModel!!.add(DataModel("Gingerbread", true))
        dataModel!!.add(DataModel("Honeycomb", false))
        dataModel!!.add(DataModel("Ice Cream Sandwich", false))
        dataModel!!.add(DataModel("Jelly Bean", false))
        dataModel!!.add(DataModel("Kitkat", false))
        dataModel!!.add(DataModel("Lollipop", false))
        dataModel!!.add(DataModel("Marshmallow", false))
        dataModel!!.add(DataModel("Nougat", false))

        // Setting the adapter
        adapter = CustomAdapter(dataModel!!, applicationContext)
        listView.adapter = adapter

        // Upon item click, checkbox will be set to checked
        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val dataModel: DataModel = dataModel!![position]
            dataModel.checked = !dataModel.checked
            adapter.notifyDataSetChanged()
        }
    }
}