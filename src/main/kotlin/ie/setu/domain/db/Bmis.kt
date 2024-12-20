package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

// SRP - Responsibility is to manage one activity.
//       Database wise, this is the table object.

object Bmis : Table("bmis") {
    val id = integer("id").autoIncrement()
    val height = double("height")
    val weight = double("weight")
    val bmivalue = double("bmivalue")
    val started = datetime("started")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(Bmis.id, name = "PK_Bmis_ID")
}