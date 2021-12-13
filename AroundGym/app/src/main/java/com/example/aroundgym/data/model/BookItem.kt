package com.example.aroundgym.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class BookItem(
    val authors: List<String>,
    val contents: String,
    val datetime: String,
    val isbn: String,
    val price: Int,
    val publisher: String,
    val sale_price: Int,
    val status: String,
    val thumbnail: String,
    val title: String,
    val translators: List<String>,
    val url: String
) : Parcelable