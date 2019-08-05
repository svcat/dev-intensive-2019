package ru.skillbranch.devintensive.extensions

import android.app.Activity
import android.content.Context.INPUT_METHOD_SERVICE
import android.graphics.Rect
import android.view.inputmethod.InputMethodManager
import android.view.ViewGroup
import ru.skillbranch.devintensive.R

fun Activity.hideKeyboard() {
    if (currentFocus != null) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}

fun Activity.isKeyboardOpen(): Boolean {
    return isKeyboardVisible()
}

fun Activity.isKeyboardClosed(): Boolean {
   return !isKeyboardVisible()
}

private fun Activity.isKeyboardVisible(): Boolean {
    val activityRoot = findViewById<ViewGroup>(android.R.id.content)
    val rootView = getWindow().getDecorView().getRootView()
    val visible = Rect()
    rootView.getWindowVisibleDisplayFrame(visible)
    return activityRoot.height - (visible.bottom - visible.top) > 100
}
