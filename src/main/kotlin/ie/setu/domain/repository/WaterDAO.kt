package ie.setu.domain.repository

import ie.setu.domain.Water
import ie.setu.domain.db.Waters
import ie.setu.utils.mapToWater
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

//workout data access objects that handles database operations
class WaterDAO {

    //Get all the activities in the database regardless of user id
    fun getAll(): ArrayList<Water> {
        val watersList: ArrayList<Water> = arrayListOf()
        transaction {
            Waters.selectAll().map { watersList.add(mapToWater(it)) }
        }
        return watersList
    }

    //Find a specific workout by workout ids
    fun findByWaterId(id: Int): Water?{
        return transaction {
            Waters.selectAll().where { Waters.id eq id}.map{ mapToWater(it) }.firstOrNull()
        }
    }

    //Find all waters for a specific user id
    fun findByUserId(userId: Int): List<Water>{
        return transaction {
            Waters.selectAll().where {Waters.userId eq userId}.map { mapToWater(it) }
        }
    }

    //Save an workout to the database
    fun save(waters: Water){
        transaction {
            Waters.insert {
                it[waterintake] = waters.waterintake
                it[started] = waters.started
                it[userId] = waters.userId
            }
        }
    }

    //delete by user id of an sleep from database
    fun delete(id: Int): Int {
        return transaction {
            Waters.deleteWhere { Waters.id eq id}
        }
    }

    //update and sleep in the database with sleep id
    fun updateWater(id: Int, water: Water): Int{
        return transaction {
            Waters.update({ Waters.id eq id }) {
                it[waterintake] = water.waterintake
                it[started] = water.started
                it[userId] = water.userId
            }
        }
    }

}