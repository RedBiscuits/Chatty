package com.example.myapplication.models

import android.accounts.AuthenticatorDescription
import android.net.Uri
import java.util.*

class StoryModel() {
    var uri : String = ""
    var description: String? = null
    var date: String = ""

    constructor( uri: String , description: String? , date : String) : this(){
        this.uri = uri
        this.description = description
        this.date = date
    }
}