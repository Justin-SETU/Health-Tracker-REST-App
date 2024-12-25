package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Waters : Table("waters") {
    val id = integer("id").autoIncrement()
    val waterintake = double("waterintake")
    val started = datetime("started")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(Waters.id, name = "PK_Waters_ID")
}