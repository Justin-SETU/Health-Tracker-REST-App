package ie.setu.domain.db

import org.jetbrains.exposed.sql.Table

//single responsibility principle to manage one user

//users table in database
object Users : Table("users") {
    val id = integer("id").autoIncrement()
    val name = varchar("name", 100)
    val email = varchar("email", 255)
    val password = varchar("password", 64)

    override val primaryKey = PrimaryKey(id, name = "PK_Users_ID")
}
