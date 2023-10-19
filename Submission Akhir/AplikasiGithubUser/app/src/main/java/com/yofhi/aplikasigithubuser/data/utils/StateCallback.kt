package com.yofhi.aplikasigithubuser.data.utils

import android.view.View

interface StateCallback<T> {
    fun onSuccess(data: T)

    fun onLoading()

    fun onFailed(message: String?)

    val visible: Int
        get() = View.VISIBLE

    val gone: Int
        get() = View.GONE
}