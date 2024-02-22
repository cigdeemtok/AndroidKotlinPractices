package com.example.cloneinstagramudemy.model

import com.google.firebase.Timestamp
import java.util.Date

data class Posts(
    val userEmail : String,
    val description : String,
    val imageUrl : String,
    val time : String
)
