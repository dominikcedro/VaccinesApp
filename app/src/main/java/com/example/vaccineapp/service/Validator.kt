package com.example.vaccineapp.service

import android.util.Patterns

class Validator {
    fun isNotBlank(input: String): Boolean {
        return input.isNotBlank()
    }

    fun isNotZero(input: Double): Boolean {
        return input != 0.0
    }

    fun isInCollection(input: String, collection: List<String>): Boolean {
        return collection.contains(input)
    }

    fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 8 && password.contains(Regex("[0-9]")) && containsSpecialCharacter(password)
    }

    fun isValidConfirmPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

    private fun containsSpecialCharacter(input: String): Boolean {
        val specialCharPattern = Regex("[^A-Za-z0-9 ]")
        return specialCharPattern.containsMatchIn(input)
    }
}