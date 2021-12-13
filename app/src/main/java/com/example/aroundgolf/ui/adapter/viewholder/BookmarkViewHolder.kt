package com.example.aroundgolf.ui.adapter.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.aroundgolf.R
import com.example.aroundgolf.databinding.ItemBookmarkBinding
import com.example.aroundgolf.room.GolfEntity


class BookmarkViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(
        R.layout.item_bookmark, parent, false
    )
) {

    private val binding = ItemBookmarkBinding.bind(itemView)

    fun bind(
        item: GolfEntity,
        itemClickListener: BookmarkListener
    ) {

        binding.apply {
            name.text = item.name
            address.text = item.address

            bookmark.setOnClickListener {
                itemClickListener.getItemClick(item)
            }

            call.isVisible = !item.tel.isNullOrEmpty()


            call.setOnClickListener {
                val convertNumber = "tel:" + item.tel?.filter { it.isDigit() }
                itemClickListener.call(convertNumber)
            }

            findPage.setOnClickListener {
                itemClickListener.link("https://search.naver.com/search.naver?where=nexearch&sm=top_hty&fbm=1&ie=utf8&query=${item.name}")
            }
        }
    }
}

interface BookmarkListener {
    fun getItemClick(item: GolfEntity)
    fun call(number: String)
    fun link(url: String?)
}