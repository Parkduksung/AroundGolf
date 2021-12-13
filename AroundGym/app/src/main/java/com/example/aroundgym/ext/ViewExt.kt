package com.example.aroundgym.ext

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide


fun AppCompatActivity.hideKeyboard() {
    (getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager).apply {
        hideSoftInputFromWindow(
            currentFocus!!.windowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }
}

fun Fragment.showToast(context: Context = this.requireContext(), message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


@BindingAdapter("bind:setUrlImg")
fun ImageView.setUrlImg(url: String?) {
    Glide.with(context)
        .load(url)
        .centerCrop()
        .error(android.R.drawable.ic_delete)
        .into(this)
}