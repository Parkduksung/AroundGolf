package com.example.aroundgym.api.response

import com.example.aroundgym.data.model.BookItem
import com.google.gson.annotations.SerializedName

data class KakaoSearchBookResponse(
    @SerializedName("documents") val documents: List<Document>,
    @SerializedName("meta") val meta: Meta
)

data class Document(
    @SerializedName("authors") val authors: List<String>,
    @SerializedName("contents") val contents: String,
    @SerializedName("datetime") val datetime: String,
    @SerializedName("isbn") val isbn: String,
    @SerializedName("price") val price: Int,
    @SerializedName("publisher") val publisher: String,
    @SerializedName("sale_price") val sale_price: Int,
    @SerializedName("status") val status: String,
    @SerializedName("thumbnail") val thumbnail: String,
    @SerializedName("title") val title: String,
    @SerializedName("translators") val translators: List<String>,
    @SerializedName("url") val url: String
) {

    fun toBookItem(): Pair<BookItem, Boolean> =
        Pair(
            BookItem(
                authors,
                contents,
                datetime,
                isbn,
                price,
                publisher,
                sale_price,
                status,
                thumbnail,
                title,
                translators,
                url
            ),
            false
        )
}

data class Meta(
    @SerializedName("is_end") val is_end: Boolean,
    @SerializedName("pageable_count") val pageable_count: Int,
    @SerializedName("total_count") val total_count: Int
)
