package ie.setu.domain.repository

import ie.setu.domain.User
import ie.setu.domain.db.Users
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import ie.setu.utils.mapToUser


class UserDAO {

    private val users = arrayListOf<User>(
        User(name = "Alice", email = "alice@wonderland.com", id = 0),
        User(name = "Bob", email = "bob@cat.ie", id = 1),
        User(name = "Mary", email = "mary@contrary.com", id = 2),
        User(name = "Carol", email = "carol@singer.com", id = 3)
    )

    fun getAll(): ArrayList<User> {
        val userList: ArrayList<User> = arrayListOf()
        transaction {
            Users.selectAll().map {
                userList.add(mapToUser(it))
            }
        }
        return userList
    }

    fun findById(id: Int): User?{
        return null
    }

    fun save(user: User){
        users.add(user)
    }

    fun findByEmail(email: String) :User?{
        return null
    }

    fun delete(id: Int) {
        val user = findById(id)
        users.remove(user)
    }

    fun update(id: Int, user: User){
        var foundUser = findById(id)
        foundUser?.email = user.email
        foundUser?.name = user.name
        foundUser?.id = user.id
    }

}