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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Objects.requireNonNull(supportActionBar)!!.hide() //hide the title bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_profile)

        fireStoreRef.collection("users").document(phone.toString()).get().addOnSuccessListener {
            var image = it.get("profileImageUrl").toString()
            var name = it.get("name").toString()
            var bio = it.get("description").toString()

            Glide.with(this).load(image).diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(profileImageView)
            nameText.text = name
            phoneText.text = phone
            bioText.text = bio
        }

            .addOnFailureListener {
                Toast.makeText(this , it.message , Toast.LENGTH_LONG).show()
            }
    }


    fun prefUser(key : String): String? {
        val pref = this.getSharedPreferences("mypref", AppCompatActivity.MODE_PRIVATE)
        return pref.getString(key , "012345678")
    }
}
