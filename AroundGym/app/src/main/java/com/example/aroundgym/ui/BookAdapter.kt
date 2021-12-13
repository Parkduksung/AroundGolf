package com.example.aroundgym.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.aroundgym.BR
import com.example.aroundgym.R
import com.example.aroundgym.data.model.BookItem
import com.example.aroundgym.databinding.ItemBookBinding


class BookAdapter : RecyclerView.Adapter<BookViewHolder>() {

    private lateinit var itemClickListener: (item: Pair<BookItem, Boolean>) -> Unit

    private val bookList = mutableListOf<Pair<BookItem, Boolean>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder =
        BookViewHolder(parent, R.layout.item_book)

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(bookList[position], itemClickListener)
    }

    override fun getItemCount(): Int =
        bookList.size

    fun addAll(list: List<Pair<BookItem, Boolean>>) {
        bookList.addAll(list)
        notifyDataSetChanged()
    }

    fun loadNextData(list: List<Pair<BookItem, Boolean>>) {
        for (item in list) {
            bookList.add(item)
            notifyItemChanged(bookList.lastIndex)
        }
    }

    fun toggleLike(pair: Pair<BookItem, Boolean>) {
        val index = bookList.indexOf(bookList.find { it.first == pair.first })
        bookList[index] = pair
        notifyItemChanged(index)
    }

    fun clear() {
        bookList.clear()
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (item: Pair<BookItem, Boolean>) -> Unit) {
        itemClickListener = listener
    }

}


class BookViewHolder(
    parent: ViewGroup,
    @LayoutRes layoutId: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutId, parent, false)) {

    private val binding = ItemBookBinding.bind(itemView)

    fun bind(item: Pair<BookItem, Boolean>, listener: (item: Pair<BookItem, Boolean>) -> Unit) {
        binding.run {
            setVariable(BR.bookItem, item)
            root.setOnClickListener { listener(item) }
        }
    }
}

