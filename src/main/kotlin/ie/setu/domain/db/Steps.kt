package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Steps : Table("steps") {
    val id = integer("id").autoIncrement()
    val distance = double("distance")
    val steps = integer("steps")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(Steps.id, name = "PK_Steps_ID")
}