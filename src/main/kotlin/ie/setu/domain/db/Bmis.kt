package ie.setu.domain.db

import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.jodatime.datetime

object Bmis : Table("bmis") {
    val id =integer("id").autoIncrement()
    val weight = double("weight")
    val height = double("height")
    val bmiCalc = double("bmiCalc")
    val timestamp = datetime("timestamp")
    val userId = integer("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)

    override val primaryKey = PrimaryKey(Bmis.id, name = "PK_BmiValues_ID")
}