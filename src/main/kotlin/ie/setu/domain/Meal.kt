package ie.setu.domain

import org.joda.time.DateTime

//Handles the data for the water tracker
data class Meal (
    var id: Int,
    var food:String,
    var calories: Double,
    var started: DateTime,
    var userId: Int
)