package com.example.artbookudemy

import android.graphics.Bitmap
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Art(
    val id : Int,
    val name : String,
    val image : Bitmap
) : Parcelable
