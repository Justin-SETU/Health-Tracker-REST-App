package ie.setu.domain

import org.joda.time.DateTime

//Handles the data for the water tracker
data class Water (
    var id: Int,
    var waterintake: Double,
    var started: DateTime,
    var userId: Int
)