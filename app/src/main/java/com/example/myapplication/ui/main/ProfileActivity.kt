package com.example.myapplication.ui.main

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.datastructures.chatty.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*

class ProfileActivity : AppCompatActivity() {

    private val fireStoreRef by lazy {
        FirebaseFirestore.getInstance()
    }

    private val phone : String? by lazy {
        prefUser("phone")
    }
    private val username : String? by lazy {
        prefUser("name")
    }
    private val profileImage : String? by lazy {
        prefUser("image")
    }

    private val bio : String? by lazy {
        prefUser("bio")
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Objects.requireNonNull(supportActionBar)!!.hide() //hide the title bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile)

        Glide.with(this).load(profileImage).diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(profileImageView)
        nameText.text = username
        phoneText.text = phone
        bioText.text = bio

    }


    fun prefUser(key : String): String? {
        val pref = this.getSharedPreferences("mypref", AppCompatActivity.MODE_PRIVATE)
        return pref.getString(key , null)
    }
}
