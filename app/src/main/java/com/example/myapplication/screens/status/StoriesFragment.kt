package com.example.myapplication.screens.status
import Statue_Adapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.datastructures.chatty.R
import com.example.myapplication.models.StoryModel
import com.example.myapplication.models.UserModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_stories.*
import omari.hamza.storyview.StoryView
import omari.hamza.storyview.model.MyStory
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class StoriesFragment : Fragment(R.layout.fragment_stories) {


    val uid : String? by lazy {
        restorePrefTheme("phone")
    }
    private fun restorePrefTheme(key : String): String? {
        val pref = requireContext().getSharedPreferences("mypref", AppCompatActivity.MODE_PRIVATE)
        return  pref.getString(key, "01017046725").toString()

    }
    private val fireStoreRef by lazy {
        FirebaseFirestore.getInstance()
    }

    private val databaseRef by lazy {
        Firebase.database.reference
    }

//    var uid = "01017046725"


    val storyOfFriends : ArrayList<UserModel> by lazy {
        ArrayList()
    }

    lateinit var myUser : UserModel

    val adapter = Statue_Adapter()


    override fun onResume() {
        super.onResume()
        Log.d("gameeeeeeeeed" , uid.toString())
        fireStoreRef.collection("users").document(uid!!).get()
            .addOnSuccessListener {

                val simpleDateFormat = SimpleDateFormat("dd/M/yyyy HH:mm:ss")

                var friends = it.get("friends") as ArrayList<String>

                if (friends.size != 0) {
                    for (friend in friends) {
                        Log.d("aaaaaaaaa" , friend)

                        fireStoreRef.collection("users").document(friend).get()
                            .addOnSuccessListener {
                                    itFriend->
                                Log.d("aaaaaaaaa" , friend)

                                if (itFriend.getBoolean("hasStory") == true) {


                                    databaseRef.child("users").child(friend)
                                        .addValueEventListener(object : ValueEventListener {

                                            override fun onDataChange(dataSnapShot: DataSnapshot) {
                                                storyOfFriends.clear()
                                                val stories: ArrayList<MyStory> = ArrayList()
                                                var lastStory = StoryModel( "" , "" , "")

                                                for (snapShot in dataSnapShot.children) {

                                                    val story = snapShot.getValue<StoryModel>()
                                                    lastStory = story!!

                                                    val currentDate = simpleDateFormat.format(Date())
                                                    if (currentDate != null) {


                                                        val diff =
                                                            (simpleDateFormat.parse(
                                                                currentDate
                                                            ).time - simpleDateFormat.parse(
                                                                story!!.date
                                                            ).time)
                                                        val seconds = diff / 1000
                                                        val minutes = seconds / 60
                                                        val hours = minutes / 60
                                                        if (hours >= 24) {
                                                            snapShot.ref.removeValue()
                                                        }

                                                        stories.add(
                                                            MyStory(
                                                                story!!.uri,
                                                                simpleDateFormat.parse(
                                                                    story.date
                                                                ),
                                                                story.description
                                                            )
                                                        )
                                                    } else {
                                                        fireStoreRef.collection("users").document(it.id).update("hasStory" , false)
                                                        break
                                                    }
                                                }


                                                val currentDate = simpleDateFormat.format(Date())
                                                if (currentDate != null) {

                                                    Log.d("curr" , currentDate)
                                                    Log.d("last" , lastStory.date)
                                                    val diff = (simpleDateFormat.parse(currentDate).time - simpleDateFormat.parse(lastStory!!.date).time)
                                                    val seconds = diff / 1000
                                                    val minutes = seconds / 60
                                                    val hours = minutes / 60
                                                    var lastStoryTime = ""

                                                    if (hours == 0L) {
                                                        lastStoryTime =
                                                            "${(minutes % 60)} minutes ago"
                                                    } else {
                                                        lastStoryTime =
                                                            "$hours:${(minutes % 60)}"

                                                    }

                                                    storyOfFriends.add(
                                                        UserModel(
                                                            stories,
                                                            itFriend.get("name").toString(),
                                                            lastStoryTime,
                                                            itFriend.getBoolean("hasStory")!!,
                                                            itFriend.get("friends") as ArrayList<String>,
                                                            itFriend.get("storyUrl").toString(),
                                                            itFriend.get("profileImageUrl").toString()
                                                        )
                                                    )
                                                }

                                                adapter.submitList(storyOfFriends)



                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                                Toast.makeText(
                                                    requireContext(),
                                                    error.message,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }

                                        })


                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG)
                                    .show()
                            }

                    }
                }



                if (it.getBoolean("hasStory") == true){

                    databaseRef.child("users").child(uid!!).orderByChild("date").addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(dataSnapShot: DataSnapshot) {

                            val myStories: ArrayList<MyStory> = ArrayList<MyStory>()

                            var lastStory = StoryModel( "" , "" , "")
                            for (snapShot in dataSnapShot.children){
                                val story = snapShot.getValue<StoryModel>()

                                lastStory = story!!
                                val currentDate = simpleDateFormat.format(Date())
                                if (currentDate != null) {
                                    val diff =
                                        (simpleDateFormat.parse(currentDate).time - simpleDateFormat.parse(
                                            story!!.date
                                        ).time)
                                    val seconds = diff / 1000
                                    val minutes = seconds / 60
                                    val hours = minutes / 60
                                    Log.d("hours", hours.toString())

                                    if (hours >= 24) {
                                        snapShot.ref.removeValue()
                                    }else {
                                        showMyStory()
                                    }

                                    myStories.add(
                                        MyStory(
                                            story.uri,
                                            simpleDateFormat.parse(story.date),
                                            story.description
                                        )
                                    )
                                } else {
                                    fireStoreRef.collection("users").document(uid!!).update("hasStory" , false)
                                    hideMyStory()
                                }
                            }

                            var hastStory = myStories.size > 0

                            if (hastStory) {


                                val currentDate = simpleDateFormat.format(Date())
                                Log.d("current", currentDate)
                                val diff =
                                    (simpleDateFormat.parse(currentDate).time - simpleDateFormat.parse(
                                        lastStory!!.date
                                    ).time)
                                val seconds = diff / 1000
                                val minutes = seconds / 60
                                val hours = minutes / 60
                                var lastStoryTime = ""

                                if (currentDate.substring(0, 2) == lastStory.date.substring(0, 2)) {
                                    day.text = "Today"
                                } else {
                                    day.text = "Yesterday"
                                }

                                if (hours == 0L && minutes == 0L) {
                                    lastStoryTime = "now"
                                    day.visibility = View.GONE
                                } else if (hours == 0L) {
                                    day.visibility = View.GONE
                                    lastStoryTime = "${(minutes % 60)} minutes ago"
                                } else {
                                    day.visibility = View.VISIBLE

                                    if (lastStory.date.substring(10, 12).toInt() > 12) {
                                        if ((lastStory.date.substring(10, 12).toInt() - 12) >= 10) {
                                            lastStoryTime = "${
                                                (lastStory.date.substring(10, 12).toInt() - 12)
                                            }:${lastStory.date.substring(13, 15)} PM"

                                        } else {
                                            lastStoryTime = "${
                                                (lastStory.date.substring(11, 12).toInt() - 12)
                                            }:${lastStory.date.substring(13, 15)} PM"

                                        }
                                    } else if (lastStory.date.substring(10, 12).toInt() < 12) {
                                        if ((lastStory.date.substring(10, 12).toInt() >= 10)) {
                                            lastStoryTime = "${
                                                (lastStory.date.substring(
                                                    10,
                                                    12
                                                ))
                                            }:${lastStory.date.substring(13, 15)} AM"
                                        } else {
                                            lastStoryTime = "${
                                                (lastStory.date.substring(
                                                    11,
                                                    12
                                                ))
                                            }:${lastStory.date.substring(13, 15)} AM"
                                        }
                                    }

                                }

                                myUser = UserModel(
                                    myStories,
                                    it.get("name").toString(),
                                    lastStoryTime,
                                    hastStory,
                                    friends,
                                    it.get("storyUrl").toString(),
                                    it.get("profileImageUrl").toString()
                                )
                            } else {
                                myUser = UserModel(
                                    myStories,
                                    it.get("name").toString(),
                                    "",
                                    hastStory,
                                    friends,
                                    it.get("storyUrl").toString(),
                                    it.get("profileImageUrl").toString()
                                )
                            }


                            myLastStory.text = myUser.lastStory
                            myStoryTitle.text = myUser.name
                            Glide.with(requireContext()).load(myUser.storyUrl).into(myStoryImg)




                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(requireContext() , error.message , Toast.LENGTH_LONG).show()
                        }

                    })



                } else {
                    hideMyStory()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext() , it.message , Toast.LENGTH_LONG).show()
            }

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        myStoryImg.setOnClickListener {
            myLastStory.text = myUser.lastStory
            StoryView.Builder(parentFragmentManager)
                .setStoriesList(myUser.stories)
                .setStoryDuration(5000)
                .setTitleText(myUser.name)
                .setTitleLogoUrl(myUser.imageUri)
                .setStartingIndex(0)
                .setOnStoryChangedCallback { position ->

                }.build()
                .show()

            onResume()


        }

        rvStatus.adapter = adapter
        rvStatus.layoutManager = LinearLayoutManager(requireContext())

        adapter.setOnItemClickListner {
            myLastStory.text = myUser.lastStory
            StoryView.Builder(parentFragmentManager)
                .setStoriesList(it.stories)
                .setStoryDuration(5000)
                .setTitleText(it.name)
                .setTitleLogoUrl(it.imageUri)
                .setStartingIndex(0)
                .build()
                .show()

        }



        addTextStory.setOnClickListener {

            addStory.collapseImmediately()
            val intent : Intent = Intent(this.activity, AddTextStoryActivity::class.java)
            intent.putExtra("phone" , uid)
            startActivity(intent)
        }

        addImagedStory.setOnClickListener {

            addStory.collapseImmediately()
            val intent : Intent = Intent(this.activity, AddImageStoryActivity::class.java)
            intent.putExtra("phone" , uid)
            startActivity(intent)

        }
    }



    fun hideMyStory(){
        myLastStory.visibility = View.GONE
        myStoryImg.visibility = View.GONE
        myStoryTitle.visibility = View.GONE
        lineView.visibility = View.GONE
    }




    fun showMyStory(){
        myLastStory.visibility = View.VISIBLE
        myStoryImg.visibility = View.VISIBLE
        myStoryTitle.visibility = View.VISIBLE
        lineView.visibility = View.VISIBLE
    }
}
