package ie.setu.domain.db

import ie.setu.domain.Workout
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Logs : Table("logs") {
    val id = integer("id").autoIncrement()
    val status_type = varchar("status_type", 100)
    val summary = varchar("summary", 100)
    val started = datetime("started")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(Workouts.id, name = "PK_Workouts_ID")
}