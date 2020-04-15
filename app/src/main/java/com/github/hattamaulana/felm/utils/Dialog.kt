package com.github.hattamaulana.felm.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.singleChoiceDialog(
    title: String, string: Array<String>,
    textPositiveButton: String = "OK",
    onPositiveClicked: (checked: Int)-> Unit
): AlertDialog {
    var checked = -1
    return AlertDialog.Builder(this)
        .setTitle(title)
        .setSingleChoiceItems(string, checked) { _, which ->
            checked = which
        }
        .setPositiveButton(textPositiveButton) { _, _ ->
            onPositiveClicked(checked)
        }
        .create()
}