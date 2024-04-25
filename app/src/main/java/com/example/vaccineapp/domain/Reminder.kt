package com.example.vaccineapp.domain

import java.time.ZonedDateTime

data class Reminder(
    var dateTime: ZonedDateTime?
) {
    constructor(): this(null)
}