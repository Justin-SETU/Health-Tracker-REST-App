package ie.setu.domain

import org.joda.time.DateTime

//Handles the data for the workout tracker
data class Log (
    var id: Int,
    var status_type: String,
    var summary: String,
    var started: DateTime,
    var userId: Int
)

