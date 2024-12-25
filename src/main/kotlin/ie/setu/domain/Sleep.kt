package ie.setu.domain

import org.joda.time.DateTime

//Handles the data for the sleep tracker
data class Sleep (
    var id: Int,
    var duration: Double,
    var started: DateTime,
    var userId: Int
)