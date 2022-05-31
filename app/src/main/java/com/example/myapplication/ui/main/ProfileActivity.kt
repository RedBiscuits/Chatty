package com.example.myapplication.ui.main

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.datastructures.chatty.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.login_tab_fragment.*
import kotlin.collections.ArrayList
import kotlin.jvm.internal.Intrinsics

public class ProfileActivity : AppCompatActivity() {

    private lateinit var selectedImgUri : Uri

    private val fireStoreRef by lazy {
        FirebaseFirestore.getInstance()
    }



    private val phone : String by lazy {
        prefUser("phone")
    }
    private val username : String by lazy {
        prefUser("name")
    }
    private val profileImage : String by lazy {
        prefUser("image")
    }

    private lateinit var bio : String



    val profId by lazy {
        intent.getStringExtra("phone")
    }
    var me = false
    var isFriend = false
    var privacy= false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        try {
            this.supportActionBar!!.hide();
        }catch (e : Exception){
            e.printStackTrace()
        }

        me = profId == phone
        checkStoragePermission()

        // Get My Data
        fireStoreRef.collection("users").document(phone).get().addOnSuccessListener {

            bio = it.get("description").toString()
            val friends = it.get("friends") as ArrayList<String>

            if (!me){
                hideEditable()

                // check if he is a friend
                isFriend = profId in friends

                // check if im a friend for him
                if (isFriend){
                    fireStoreRef.collection("users").document(profId!!).get().addOnSuccessListener {
                        val hisFriends = it.get("friends") as ArrayList<String>
                        privacy = it.getBoolean("privacy")!!
                        isFriend = phone in hisFriends
                    }
                }



                if (isFriend){
                    showProfile(username , bio , profileImage)
                    progressBarSetting.isVisible = false
                } else {
                    if (privacy){
                        hideProfile(bio)
                        progressBarSetting.isVisible = false
                    } else{
                        showProfile(username , bio , profileImage)
                        progressBarSetting.isVisible = false
                    }
                }


            } else {
                showEditable()
                showProfile(username , bio , profileImage)
                progressBarSetting.isVisible = false
            }

        }

            .addOnFailureListener {
                Toast.makeText(this , it.message , Toast.LENGTH_LONG).show()
            }


        // Update Name on Click Action Done Button
        nameText.setOnEditorActionListener(object : TextView.OnEditorActionListener{  // Anonymous Class
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {

                if (actionId == EditorInfo.IME_ACTION_DONE){

                    if (nameText.text.trim().isNotEmpty()){

                        updateName(nameText.text.toString())
                        return true

                    } else {
                        Toast.makeText(this@ProfileActivity , "Please Enter Valid Name" , Toast.LENGTH_SHORT).show()
                    }

                }

                return true
            }

        })


        var currentName : String = ""

        enableDisableEditName.setOnClickListener{
            currentName = nameText.text.toString()
            enableDisableEditName.isVisible = false
            cancelEditName.isVisible = true
            nameText.isEnabled = true
            nameText.requestFocus()
        }

        cancelEditName.setOnClickListener {
            nameText.setText(currentName)
            nameText.isEnabled = false
            enableDisableEditName.isVisible = true
            cancelEditName.isVisible = false

        }

       profileImagee.setOnClickListener {
           pickImg()
       }


        bioText.setOnEditorActionListener(object : TextView.OnEditorActionListener{
            override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE){

                    if (bioText.text.trim().isNotEmpty()){

                        updateBio(bioText.text.toString())
                        return true

                    } else {
                        Toast.makeText(this@ProfileActivity , "Please Enter Valid Bio" , Toast.LENGTH_SHORT).show()
                    }

                }

                return true
            }

        })


        var currentBio : String = ""

        enableDisableEditBio.setOnClickListener{
            currentBio = bioText.text.toString()

            enableDisableEditBio.isVisible = false
            cancelEditBio.isVisible = true
            bioText.isEnabled = true
            bioText.requestFocus()
        }

        cancelEditBio.setOnClickListener {
            bioText.setText(currentBio)
            bioText.isEnabled = false
            enableDisableEditBio.isVisible = true
            cancelEditBio.isVisible = false

        }




    }


    private fun updateBio(bio: String) {

        fireStoreRef.collection("users").document(phone).update("description" , bio).addOnSuccessListener {
            bioText.isEnabled = false
            enableDisableEditBio.isVisible = true
            cancelEditBio.isVisible = false
            Toast.makeText(this , "Bio is successfully updated" , Toast.LENGTH_LONG).show()
        }

            .addOnFailureListener {
                Toast.makeText(this , it.message , Toast.LENGTH_LONG).show()
            }

    }


    // get my user phone number
    private fun prefUser(key : String) : String {
        val pref = this.getSharedPreferences("mypref", MODE_PRIVATE)
        return pref.getString(key , "error")!!
    }

    private fun updateName(name : String) {
        fireStoreRef.collection("users").document(phone).update("name" , name).addOnSuccessListener {
            nameText.isEnabled = false
            enableDisableEditName.isVisible = true
            cancelEditName.isVisible = false

            Toast.makeText(this , "Name is successfully updated" , Toast.LENGTH_LONG).show()
        }

            .addOnFailureListener {
                nameText.isEnabled = false
                enableDisableEditName.isVisible = true
                cancelEditName.isVisible = false

                Toast.makeText(this , it.message , Toast.LENGTH_LONG).show()
            }
    }


    //**
    private fun checkStoragePermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == -1
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                101
            )
        } else {
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_LONG).show()
        }
    }

    //**
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        Intrinsics.checkNotNullParameter(permissions, "permissions")
        Intrinsics.checkNotNullParameter(grantResults, "grantResults")
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults)
        if (requestCode == 101) {
            if (grantResults.isNotEmpty() && grantResults[0] == 0) {
                Toast.makeText(this, "Storage Permission Granted", Toast.LENGTH_LONG).show()
            } else {
                onBackPressed()
            }
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null){
            selectedImgUri = data.data!!
            sendImageToFireBase()
        }
    }


    fun pickImg(){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent , 3)
    }



    private fun sendImageToFireBase() {

        val storageRef = FirebaseStorage.getInstance().reference
        val riverRef = storageRef.child("imgProfile/$phone")


        riverRef.putFile(selectedImgUri)

            .addOnSuccessListener {

                riverRef.downloadUrl.addOnSuccessListener {

                    fireStoreRef.collection("users").document(phone).update("profileImage" , it).addOnSuccessListener {
                        Glide.with(this).load(selectedImgUri).into(profileImagee)
                        Toast.makeText(this,"Image Successfully Updated", Toast.LENGTH_LONG).show()
                    }
                        .addOnFailureListener {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                        }
                }

                    .addOnFailureListener {
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                    }

            }

            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }

    }

    fun showEditable(){
        enableDisableEditName.isVisible = true
        enableDisableEditBio.isVisible = true
        profileImagee.isEnabled = true

    }

    fun hideEditable(){
        enableDisableEditName.isVisible = false
        enableDisableEditBio.isVisible = false
        profileImagee.isEnabled = false

    }

    private fun hideProfile(bio: String) {
        phoneText.text = profId
        bioText.setText(bio)
        nameText.setText("This Account Is Private")
    }

    private fun showProfile(name: String , bio: String , image : String) {
        nameText.setText(name)
        phoneText.text = profId
        bioText.setText(bio)
        if (image != ""){
            Glide.with(this).load(image).into(profileImagee)
        }
        bioText.isVisible = true
    }



}
