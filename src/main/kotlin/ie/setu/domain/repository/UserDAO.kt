package ie.setu.domain.repository

import ie.setu.domain.LoginModel
import ie.setu.domain.User
import ie.setu.domain.db.Users
import ie.setu.utils.mapToUser
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

//user data access objects that handles database operations
class UserDAO {


    //User Login
    fun loginUser(model: LoginModel): Boolean {
        return transaction {
            Users.selectAll().where { Users.email eq model.email and (Users.password eq model.password) }
                .singleOrNull() != null
        }
    }

    //Show user details
    fun getAll(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it))
            }
        }
        return userList
    }

    //Register new user
    fun save(user: User) {
        transaction {
            Users.insert {
                it[name] = user.name
                it[email] = user.email
                it[password] = user.password
            }
        }
    }

    //delete user details
    fun delete(id: Int): Int {
        return transaction {
            Users.deleteWhere { Users.id eq id }
        }
    }

    //Update the users name, email, password
    fun update(id: Int, user: User) {
        transaction {
            Users.update({
                Users.id eq id
            }) {
                it[name] = user.name
                it[email] = user.email
                it[password] = user.password
            }
        }
    }


    //Features not included in app
    fun findById(id: Int): User? {
        return transaction {
            Users.selectAll().where { Users.id eq id }
                .map { mapToUser(it) }
                .firstOrNull()
        }
    }


    //Search user by email
    fun findByEmail(email: String): User? {
        return transaction {
            Users.selectAll().where { Users.email eq email }
                .map { mapToUser(it) }
                .firstOrNull()
        }
    }


}