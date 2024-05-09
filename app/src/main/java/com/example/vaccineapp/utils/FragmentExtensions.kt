package com.example.vaccineapp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.vaccineapp.R
import com.google.android.material.snackbar.Snackbar

/**
 * Shows a snackbar with a message.
 * @param message The message to be displayed.
 * @param error If true, the snackbar will have an error color.
 */
fun Fragment.showSnackBar(message: String, error: Boolean) {
    val snackbar = Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
    snackbar.setTextColor(
        ContextCompat.getColor(
            requireContext(),
            R.color.black
        )
    )
    if (error) {
        snackbar.setBackgroundTint(
            ContextCompat.getColor(
                requireContext(),
                R.color.snackBarError
            )

        )
    } else {
        snackbar.setBackgroundTint(
            ContextCompat.getColor(
                requireContext(),
                R.color.snackBarSuccessful
            )
        )
    }
    snackbar.show()
}

/**
 * Hides the keyboard.
 * @param view The view that has the keyboard.
 */
fun Fragment.hideKeyboard(view: View) {
    val inputMethodManager = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}