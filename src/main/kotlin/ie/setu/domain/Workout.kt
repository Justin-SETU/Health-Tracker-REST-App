package ie.setu.domain

import org.joda.time.DateTime

//Handles the data for the workout tracker
data class Workout (
    var id: Int,
    var workout: String,
    var duration: Double,
    var started: DateTime,
    var userId: Int
)

