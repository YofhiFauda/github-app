package com.yofhi.aplikasigithubuser.data.helper

import android.content.Context
import android.widget.Toast

class Helper {
    /**
     * Function to show toast
     */
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}