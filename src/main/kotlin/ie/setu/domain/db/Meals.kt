package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Meals : Table("meals") {
    val id = integer("id").autoIncrement()
    val food = varchar("food", 100)
    val calories = double("calories")
    val started = datetime("started")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(Meals.id, name = "PK_Meals_ID")
}