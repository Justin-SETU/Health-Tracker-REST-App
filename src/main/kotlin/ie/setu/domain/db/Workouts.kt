package ie.setu.domain.db

import ie.setu.domain.Workout
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Workouts : Table("workouts") {
    val id = integer("id").autoIncrement()
    val workout = varchar("workout", 100)
    val duration = double("duration")
    val started = datetime("started")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(Workouts.id, name = "PK_Workouts_ID")
}