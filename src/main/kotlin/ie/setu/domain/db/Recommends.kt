package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Recommends : Table("recommends") {
    val id = integer("id").autoIncrement()
    val workout = varchar("workout", 100)
    val recommend = varchar("recommend", 100)
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(Recommends.id, name = "PK_Recommends_ID")
}