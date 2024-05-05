package com.example.vaccineapp.domain

import java.time.ZonedDateTime

/**
 * Data class for reminder.
 */
data class Reminder(
    var dateTime: ZonedDateTime?
) {
    constructor(): this(null)
}