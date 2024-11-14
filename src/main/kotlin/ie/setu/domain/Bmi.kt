package ie.setu.domain

import org.joda.time.DateTime

data class Bmi (var id: Int,
                var height: Double,
                var weight: Double,
                var bmivalue: Double,
                var started: DateTime,
                var userId: Int)